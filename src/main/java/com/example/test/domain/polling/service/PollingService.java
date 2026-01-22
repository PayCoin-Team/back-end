package com.example.test.domain.polling.service;

import com.example.test.domain.polling.config.TronProperties;
import com.example.test.domain.polling.dto.TronGridResponse;
import com.example.test.domain.polling.dto.TronTransfer;
import com.example.test.domain.polling.model.Polling;
import com.example.test.domain.polling.repository.PollingRepository;
import com.example.test.domain.transaction.entity.Status;
import com.example.test.domain.transaction.entity.Transaction;
import com.example.test.domain.transaction.entity.Type;
import com.example.test.domain.transaction.repository.TransactionRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollingService {

    private final TronProperties properties;
    private final PollingRepository pollingRepository;
    private final WebClient tronGridClient;
    private final TransactionRepository transactionRepository;
    // api로 받은 금액 데이터 변환용 소수점
    private static final int USDT_DECIMALS = 6;

    // polling은 성공한 것만 DB에 반영
    @Transactional
    public void pollingUSDT(){
        Polling polling = pollingRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.ROW_NOT_FOUND));

        long lastTimeStamp = polling.getLastTimestamp() + 1; // DB에 저장된 timestamp
        long newTimeStamp = polling.getLastTimestamp();; // 갱신 후 새로 저장할 timestamp
        String fingerprint = null;

        while (true) {
            final String fp = fingerprint;
            TronGridResponse tron;

            try {
                tron = tronGridClient.get()
                        .uri(uriBuilder -> {
                            var b = uriBuilder
                                    .path("/v1/accounts/{wallet}/transactions/trc20")
                                    .queryParam("contract_address", properties.token().usdtContract())
                                    .queryParam("only_confirmed", true)
                                    .queryParam("order_by", "block_timestamp,asc")
                                    .queryParam("min_timestamp", lastTimeStamp)
                                    .queryParam("limit", 200);

                            // api에서 받아온 데이터에 다음 페이지가 있으면 계속 가져오게 설정
                            if (fp != null && !fp.isBlank()) {
                                b.queryParam("fingerprint", fp);
                            }
                            return b.build(properties.wallet().serverAddress());
                        })
                        .retrieve() // 응답 받기
                        .bodyToMono(TronGridResponse.class) // 응답 바디 객체로 변환
                        .block();
            }catch (WebClientResponseException e){
                throw new CustomException(ErrorCode.TRON_API_ERROR);
            }

            if (tron == null || tron.data() == null)
                throw new CustomException(ErrorCode.TRON_API_ERROR);

            // transfer 이벤트들 추출
            List<TronTransfer> transfers = tron.data();

            // 입출금 처리
            handleTransfer(transfers);

            if (!transfers.isEmpty()) {
                long pageMax = transfers.stream()
                        .mapToLong(TronTransfer::blockTimestamp)
                        .max()
                        .orElse(newTimeStamp);

                newTimeStamp = Math.max(newTimeStamp, pageMax);
            }

            // 다음 페이지 조회를 위한 fingerprint 추출
            String nextPage = null;
            if (tron.meta() != null) nextPage = tron.meta().fingerprint();
            if (nextPage == null || nextPage.isBlank()) break;
            fingerprint = nextPage;
        }

        // timestamp 갱신
        if (newTimeStamp > polling.getLastTimestamp()) {
            polling.setLastTimestamp(newTimeStamp);
            log.info("polling last_timestamp 업데이트");
        } else {
            log.info("새로운 트랜잭션 없음");
        }
    }


    // 입출금 확인 및 검증 후 DB 반영
    private void handleTransfer(List<TronTransfer> transfers) {

        String serverWallet = properties.wallet().serverAddress();

        if(serverWallet == null || serverWallet.isBlank()) throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);

        if (transfers == null || transfers.isEmpty()) return;

        for (TronTransfer t : transfers) {
            String txId = t.transactionId();
            String from = t.from();
            String to = t.to();
            // String값 변환
            BigDecimal value = new BigDecimal(new BigInteger(t.value()))
                    .movePointLeft(USDT_DECIMALS);

            // 입금
            if(serverWallet.equalsIgnoreCase(to)){
                Transaction ts = transactionRepository.findByTxid(txId).orElse(null);

                if(ts == null || ts.getStatus() == Status.COMPLETED) continue;

                if(!ts.getType().equals(Type.DEPOSIT) || ts.getAmount().compareTo(value) != 0) {
                    log.warn("입금 검증 오류 txId: {} from: {}: to={}", txId, from, to);
                    continue;
                }

                ts.setStatus(Status.COMPLETED);
            }

            //출금
            else if(serverWallet.equalsIgnoreCase(from)){
                Transaction ts = transactionRepository.findByTxid(txId).orElse(null);

                if(ts == null || ts.getStatus() == Status.COMPLETED ) continue;

                if(!ts.getType().equals(Type.WITHDRAW) || ts.getAmount().compareTo(value) != 0) {
                    log.warn("출금 검증 오류 txId: {} from: {}: to={}", txId, from, to);
                    continue;
                }

                ts.setStatus(Status.COMPLETED);
            }
        }

    }
}
