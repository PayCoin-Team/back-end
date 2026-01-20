package com.example.test.domain.polling.service;

import com.example.test.domain.polling.config.TronProperties;
import com.example.test.domain.polling.dto.TronEventData;
import com.example.test.domain.polling.dto.TronGridResponse;
import com.example.test.domain.polling.model.Polling;
import com.example.test.domain.polling.repository.PollingRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollingService {

    private final TronProperties properties;
    private final PollingRepository pollingRepository;

    @Transactional
    public void applyLatestBlockAndProcessEvents(TronGridResponse response) {

        // 받은 데이터가 없는 경우
        if (response == null || !response.success() || response.data() == null)
            throw new CustomException(ErrorCode.TRON_API_ERROR);

        List<TronEventData> events = response.data();

        if (events.isEmpty()) {
            log.info("새로운 트랜잭션이 없음");
            return;
        }

        // 입출금 이벤트 처리
        processEvents(events);

        // lastBlock 업데이트
        long latestBlock = events.stream().mapToLong(TronEventData::blockNumber)
                .max().orElseThrow();

        Polling polling = pollingRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.ROW_NOT_FOUND));


        if (latestBlock > polling.getLastBlock()) {
            polling.setLastBlock(latestBlock);
            log.info("폴링 완료: {} 번 블록까지 최신화되었습니다.", latestBlock);
        } else {
            log.info("폴링 완료");
        }

    }

    // 입출금 이벤트 처리
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
