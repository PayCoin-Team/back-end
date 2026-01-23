package com.example.test.domain.transaction.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.contract.Contract;
import org.tron.trident.core.contract.Trc20Contract;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@Service
public class TronRawService {

    // application.yml의 값들을 변수에 주입 (Injection)
    @Value("${tron.wallet.private-key}")
    private String privateKey;

    @Value("${tron.wallet.server-address}")
    private String fromAddress;

    @Value("${tron.token.usdt-contract}")
    private String usdtContractAddress;

    @Value("${tron.trongrid.base-url}")
    private String trongridBaseUrl; // "https://nile.trongrid.io"

    /**
     * Fuck! 트론 라이브러리. 서비스 지갑에게 출금 요청 함수
     * @Param toAddress 사용자 지갑 주소(String)
     * @Param amount 금액 파라미터로 입력 받음
     * */
    public String sendUSDT(String toAddress, BigDecimal amount) {
        try {
            // ApiWrapper 생성
            ApiWrapper client;
            if (trongridBaseUrl.contains("nile")) {
                client = ApiWrapper.ofNile(privateKey);
            } else {
                client = ApiWrapper.ofMainnet(privateKey, "YOUR_API_KEY");
            }

            // 금액 변환 (USDT 소수점 6자리)
            BigInteger amountInSun = amount.multiply(BigDecimal.valueOf(1_000_000)).toBigInteger();

            log.info("============== [CrossPay 전송 요청] ==============");
            log.info("From: {}", fromAddress);
            log.info("To: {}", toAddress);
            log.info("Amount: {} USDT", amount);

            // Contract 객체 로드 (Trident 기능 사용)
            Contract baseContract = client.getContract(usdtContractAddress);

            // TRC-20 래퍼 연결
            Trc20Contract token = new Trc20Contract(baseContract, fromAddress, client);

            // 블록체인 전송 실행
            String txId = token.transfer(toAddress, amountInSun.longValue(), 0, "CrossPay", 100_000_000L);

            log.info("전송 완료! TxID: {}", txId);
            return txId;

        } catch (Exception e) {
            log.error("전송 실패: {}", e.getMessage());
            throw new RuntimeException("USDT 전송 중 오류가 발생했습니다.", e);
        }
    }
}