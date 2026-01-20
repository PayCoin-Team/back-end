package com.example.test.domain.history.service;

import com.example.test.domain.history.dto.request.RequestTransferDto;
import com.example.test.domain.history.dto.response.ResponseTransferDto;
import com.example.test.domain.history.entity.History;
import com.example.test.domain.history.repository.HistoryRepository;
import com.example.test.domain.user.repository.UserRepository;
import com.example.test.domain.userwallet.entity.UserWallet;
import com.example.test.domain.userwallet.repository.UserWalletRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {

    private final UserRepository userRepository;
    private final UserWalletRepository userWalletRepository;
    private final HistoryRepository historyRepository;

    // 내부 송금 요청
    public ResponseTransferDto requestTransfer(Long userId, RequestTransferDto dto) {

        // 보내는 사람 지갑
        UserWallet senderWallet = userWalletRepository.findByUserIdWithLock(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_WALLET_NOT_FOUND));
        // 받는 사람 지갑
        UserWallet receiverWallet = userWalletRepository.findByPublicAddress(dto.targetAddress())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_WALLET_NOT_FOUND));

        // 자신에게 송금 차단
        if(senderWallet.equals(receiverWallet)) {
            throw new CustomException(ErrorCode.SELF_TRANSFER_INVALID);
        }

        // 잔액 확인
        if(senderWallet.getBalance().compareTo(dto.amount()) < 0 ) {
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(dto.amount()));
        receiverWallet.setBalance(receiverWallet.getBalance().add(dto.amount()));

        History history = dto.dtoToEntity(senderWallet, receiverWallet);
        historyRepository.save(history);

        return new ResponseTransferDto(history.getId(), senderWallet.getBalance());
    }
}
