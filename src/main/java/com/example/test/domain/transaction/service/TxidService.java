package com.example.test.domain.transaction.service;

import com.example.test.domain.polling.config.TronProperties;
import com.example.test.domain.transaction.dto.TransferInfo;
import com.example.test.domain.transaction.dto.TronTxidResponse;
import com.example.test.domain.transaction.enums.Status;
import com.example.test.domain.transaction.entity.Transaction;
import com.example.test.domain.transaction.repository.TransactionRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.tron.trident.utils.Base58Check;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TxidService {
    private final TronProperties properties;
    private final @Qualifier("tronGridWebClient") WebClient tronGridClient;

    // /wallet/gettransactioninfobyid에서 받아온 데이터의 transfer 구분을 위한 문자열
    private static final String TRANSFER =
            "ddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
    private static final int USDT_DECIMALS = 6;

    @Transactional
    public TransferInfo findInfoByTxid(String txid) {

        TronTxidResponse tron;

        try {
            tron = tronGridClient.post()
                    .uri("/wallet/gettransactioninfobyid")
                    .bodyValue(Map.of("value", txid))
                    .retrieve()
                    .bodyToMono(TronTxidResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            // txid가 아직 네트워크에 없는 경우(404)를 PENDING으로 처리
            int code = e.getStatusCode().value();

            if (code == 404) {
                tron = null;
            } else {
                throw new CustomException(ErrorCode.TRON_API_ERROR);
            }
        }

        if (tron == null) return null;

        // 아직 처리 안됐으면 PENDING 리턴
        if (!isSuccess(tron)) {
            throw new CustomException(ErrorCode.TRANSFER_FAILED); // 혹은 별도 처리
        }

        // 검증
        return verifyEvent(tron);
    }

    public boolean isPending(TronTxidResponse resp) {
        if (resp == null) return true;
        if (resp.blockNumber() == null) return true;
        if (resp.receipt() == null) return true;
        return resp.receipt().result() == null || resp.receipt().result().isBlank();
    }

    public boolean isSuccess(TronTxidResponse resp) {
        return resp != null
                && resp.receipt() != null
                && "SUCCESS".equalsIgnoreCase(resp.receipt().result());
    }

    // hex 주소를 Base58로 변환하는 함수, tron에서 운영하는 trident 라이브러리 사용
    private String convertHexToBase58(String hexAddress) {
        if (hexAddress == null || hexAddress.isBlank()) return null;

        //공백제거
        hexAddress = hexAddress.trim();

        // hex값에 0x붙어있으면 제거
        if (hexAddress.startsWith("0x") || hexAddress.startsWith("0X"))
            hexAddress = hexAddress.substring(2);

        // 만약 64자리라면 앞의 24자리(0)를 떼어내고 41을 붙임
        if (hexAddress.length() == 64) {
            hexAddress = "41" + hexAddress.substring(24);
        }

        // 41이 없는 경우 41을 붙임
        if (hexAddress.length() == 40) {
            hexAddress = "41" + hexAddress;
        }

        // 변환 로직
        byte[] raw = Hex.decode(hexAddress);
        return Base58Check.bytesToBase58(raw);
    }

    // 가져온 이벤트에 서버주소가 포함되어 있는지 확인
    private TransferInfo verifyEvent(TronTxidResponse tron) {
        if (tron == null || tron.log() == null || tron.log().isEmpty()) return null;

        String usdtContract = properties.token().usdtContract();

        for (TronTxidResponse.LogEntry log : tron.log()) {
            if (log == null || log.topics() == null || log.topics().size() < 3) continue;

            // 컨트랙트 주소 확인
            String tokenBase58 = convertHexToBase58(log.address());
            if (!usdtContract.equalsIgnoreCase(tokenBase58)) continue;

            // Transfer 이벤트 확인
            String topic0 = topicLower(log.topics().get(0));
            if (!TRANSFER.equals(topic0)) continue;

            // 정보 추출
            String from = convertHexToBase58(log.topics().get(1));
            String to   = convertHexToBase58(log.topics().get(2));

            // 금액 파싱
            BigInteger rawAmount = BigInteger.ZERO;
            if (log.data() != null && !log.data().isBlank()) {
                rawAmount = new BigInteger(topicLower(log.data()), 16);
            }
            BigDecimal amount = new BigDecimal(rawAmount).movePointLeft(USDT_DECIMALS);

            return new TransferInfo(from, to, amount);
        }
        return null;
    }

    // api로 받은 데이터가 transfer인지 확인하기 쉽게하기 위해 필요한 부분만 추출
    private String topicLower(String s) {
        if (s == null) return "";
        String t = s.trim().toLowerCase();
        return t.startsWith("0x") ? t.substring(2) : t;
    }
}
