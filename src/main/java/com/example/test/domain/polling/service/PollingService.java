package com.example.test.domain.polling.service;

import com.example.test.domain.polling.config.TronProperties;
import com.example.test.domain.polling.dto.TronGridResponse;
import com.example.test.domain.polling.dto.TronTransfer;
import com.example.test.domain.polling.model.Polling;
import com.example.test.domain.polling.repository.PollingRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollingService {

    private final TronProperties properties;
    private final PollingRepository pollingRepository;
    private final WebClient tronGridClient;

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
                        .retrieve()
                        .bodyToMono(TronGridResponse.class)
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


    // TODO: 입출금인지 확인 후 DB반영 처리 함수
    private void handleTransfer(List<TronTransfer> transfers) {}
}
