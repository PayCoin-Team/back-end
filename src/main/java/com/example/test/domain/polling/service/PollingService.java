package com.example.test.domain.polling.service;

import com.example.test.domain.polling.config.TronProperties;
import com.example.test.domain.polling.dto.TronEventData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollingService {

    private final TronProperties properties;

    public void processEvents(List<TronEventData> events) {
        String serverWallet = properties.wallet().serverAddress();

        // polling으로 감지된 이벤트들 각각 처리
        for (TronEventData event : events) {
            String fromAddress = event.result().from();
            String toAddress = event.result().to();
            String txId = event.transactionId();

            // 입금 감지: 받는 사람이 서버 지갑인 경우
            if (serverWallet.equalsIgnoreCase(toAddress)) {
                log.info("입금 감지");
                // TODO: DB에서 txId 중복 확인 후, 해당 사용자의 내부 원장 잔액 증가 로직
            }

            // 출금 확정 감지: 보낸 사람이 서버 지갑인 경우
            else if (serverWallet.equalsIgnoreCase(fromAddress)) {
                log.info("출금 감지");

                // TODO: DB에서 txId 중복 확인 후, 해당 사용자의 내부 원장 잔액 감소 로직
            }
        }
    }
}
