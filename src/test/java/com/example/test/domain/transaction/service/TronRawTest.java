package com.example.test.domain.transaction.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

// 👇 [중요] Spring AI 에러를 막기 위해 가짜 키 설정 유지
@SpringBootTest(properties = "spring.ai.openai.api-key=sk-dummy-key-for-test-execution")
class TronRawTest {

    @Autowired
    private TronRawService tronRawService;

    @Test
    @DisplayName("Trident 라이브러리로 USDT 전송 테스트")
    void sendRealUSDT() {
        // 1. 받는 사람
        String toAddress = "TEuzCSKjGJDh9gVT21N28BiZpdrAxqJNBp";

        // 2. 보낼 금액
        BigDecimal amount = BigDecimal.valueOf(10); // 10 USDT

        System.out.println("============== [테스트 시작] ==============");
        try {
            String txId = tronRawService.sendUSDT(toAddress, amount);
            System.out.println("\n✅ 최종 결과 확인: https://nile.tronscan.org/#/transaction/" + txId);
        } catch (Exception e) {
            System.out.println("\n❌ 테스트 실패");
            e.printStackTrace();
        }
        System.out.println("============== [테스트 종료] ==============");
    }
}