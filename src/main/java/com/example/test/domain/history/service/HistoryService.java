package com.example.test.domain.history.service;

import com.example.test.domain.history.dto.request.RequestTransferDto;
import com.example.test.domain.history.dto.response.ResponseHistoryDto;
import com.example.test.domain.history.dto.response.ResponseTransferDto;
import com.example.test.domain.history.entity.History;
import com.example.test.domain.history.enums.Type;
import com.example.test.domain.history.repository.HistoryRepository;
import com.example.test.domain.userwallet.entity.UserWallet;
import com.example.test.domain.userwallet.repository.UserWalletRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {

    private final UserWalletRepository userWalletRepository;
    private final HistoryRepository historyRepository;

    // 내부 송금 요청
    public ResponseTransferDto requestTransfer(Long senderId, RequestTransferDto dto) {

        UserWallet receiverPreview = userWalletRepository.findByPublicAddress(dto.targetAddress())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_WALLET_NOT_FOUND));

        Long receiverId = receiverPreview.getId();

        // 자신에게 송금 차단
        if(senderId.equals(receiverId)) {
            throw new CustomException(ErrorCode.SELF_TRANSFER_INVALID);
        }

        UserWallet senderWallet;
        UserWallet receiverWallet;

        // 교착 상태 방지 (회원 id 작은 순으로 lock 적용)
        if(senderId < receiverId) {
            senderWallet = userWalletRepository.findByUserIdWithLock(senderId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_WALLET_NOT_FOUND));
            receiverWallet = userWalletRepository.findByUserIdWithLock(receiverId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_WALLET_NOT_FOUND));
        } else {
            receiverWallet = userWalletRepository.findByUserIdWithLock(receiverId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_WALLET_NOT_FOUND));
            senderWallet = userWalletRepository.findByUserIdWithLock(senderId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_WALLET_NOT_FOUND));
        }

        // 잔액 확인 처리
        if(senderWallet.getBalance().compareTo(dto.amount()) < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(dto.amount()));
        receiverWallet.setBalance(receiverWallet.getBalance().add(dto.amount()));

        History history = dto.dtoToEntity(senderWallet, receiverWallet);
        historyRepository.save(history);

        return new ResponseTransferDto(history.getId(), senderWallet.getBalance());
    }

    // 서비스 거래 내역 조회
    @Transactional(readOnly = true)
    public Page<ResponseHistoryDto> findHistory(
            Long userId,
            Integer year,
            Integer month,
            Type type,
            Pageable pageable
    ) {

        Page<History> histories = historyRepository.searchHistories(userId, year, month, type, pageable);

        return histories.map(history -> ResponseHistoryDto.of(history, userId));
    }
}
