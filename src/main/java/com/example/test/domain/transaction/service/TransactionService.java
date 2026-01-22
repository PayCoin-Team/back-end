package com.example.test.domain.transaction.service;

import com.example.test.domain.externalWallet.entity.ExternalWallet;
import com.example.test.domain.externalWallet.repository.ExternalWalletRepository;
import com.example.test.domain.transaction.dto.request.RequestWithdrawDto;
import com.example.test.domain.transaction.dto.response.ResponseTransactionDto;
import com.example.test.domain.transaction.entity.Transaction;
import com.example.test.domain.transaction.enums.Status;
import com.example.test.domain.transaction.repository.TransactionRepository;
import com.example.test.domain.userwallet.entity.UserWallet;
import com.example.test.domain.userwallet.repository.UserWalletRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final UserWalletRepository userWalletRepository;
    private final ExternalWalletRepository externalWalletRepository;
    private final TransactionRepository transactionRepository;
    private final TronRawService tronRawService;

    @Value("${tron.wallet.server-address}")
    private String serviceWalletAddress;

    public ResponseTransactionDto withdraw(Long userId, RequestWithdrawDto dto){

        // 유저 내부 지갑 조회
        UserWallet userWallet = userWalletRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_WALLET_NOT_FOUND));

        // 외부 지갑들 조회
        List<ExternalWallet> externalWallets = externalWalletRepository.findAllByUserId(userId);
        // 저장된 외부 지갑과 입럭받은 지갑 주소 검증
        ExternalWallet targetExternalWallet = externalWallets.stream()
                .filter(wallet -> wallet.getAddress().equals(dto.walletAddress()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.EXTERNAL_WALLET_NOT_FOUND));

        // 잔액 검증
        if(userWallet.getBalance().compareTo(dto.amount()) < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }
        // 유저 내부 지갑 잔액 차감
        userWallet.setBalance(userWallet.getBalance().subtract(dto.amount()));
        // PENDING 상태로 DB에 저장
        Transaction transaction = dto.dtoToEntity(targetExternalWallet, serviceWalletAddress);
        var savedTransaction = transactionRepository.save(transaction);

        try {
            log.info("트론 네트워크로 출금 시도: Address={}, Amount={}", dto.walletAddress(), dto.amount());

            // TronRawService 호출 (실제 USDT 전송)
            String txId = tronRawService.sendUSDT(dto.walletAddress(), dto.amount());

            savedTransaction.setTxId(txId);
            savedTransaction.setStatus(Status.PROCESSING); // TODO: txId 검증 후 성공 상태로 변경시켜야 함.

            log.info("출금 성공: TxID={}", txId);
        } catch (Exception e) {
            log.error("출금 실패 (블록체인 오류): {}", e.getMessage());
            // 실패 처리 -> 깎았던 잔액 다시 돌려줌
            userWallet.setBalance(userWallet.getBalance().add(dto.amount()));
            savedTransaction.setStatus(Status.FAILED); // 실패 상태

            throw new CustomException(ErrorCode.TRANSFER_FAILED);
        }

        return ResponseTransactionDto.from(savedTransaction);
    }
}
