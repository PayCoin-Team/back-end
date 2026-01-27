package com.example.test.domain.transaction.service;

import com.example.test.domain.externalWallet.entity.ExternalWallet;
import com.example.test.domain.externalWallet.repository.ExternalWalletRepository;
import com.example.test.domain.transaction.dto.TransferInfo;
import com.example.test.domain.transaction.dto.request.RequestConfirmDepositDto;
import com.example.test.domain.transaction.enums.Type;
import com.example.test.domain.transaction.dto.request.RequestTransactionDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.tx.Transfer;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final TxidService txidService;

    @Value("${tron.wallet.server-address}")
    private String serviceWalletAddress;

    public ResponseTransactionDto deposit(
            Long userId,
            RequestTransactionDto dto
    ) {

        // 유저 내부 지갑 조회
        UserWallet userWallet = userWalletRepository.findByUserIdWithLock(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_WALLET_NOT_FOUND));
        // 유저 외부 지갑 조회
        List<ExternalWallet> externalWallets = externalWalletRepository.findAllByUserId(userId);

        ExternalWallet targetExternalWallet = externalWallets.stream()
                .filter(wallet -> wallet.getAddress().equals(dto.walletAddress()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.EXTERNAL_WALLET_NOT_FOUND));


        // 트랜잭션 기록
        Transaction transaction = dto.depositToEntity(targetExternalWallet, serviceWalletAddress);
        Transaction savedTransaction = transactionRepository.save(transaction);

        return ResponseTransactionDto.from(savedTransaction);
    }

    public ResponseTransactionDto withdraw(Long userId, RequestTransactionDto dto){

        if(dto.amount().compareTo(BigDecimal.valueOf(5)) < 0)
            throw new CustomException(ErrorCode.INVALID_BALANCE);

        // 유저 내부 지갑 조회
        UserWallet userWallet = userWalletRepository.findByUserIdWithLock(userId)
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
        Transaction transaction = dto.withdrawToEntity(targetExternalWallet, serviceWalletAddress);
        var savedTransaction = transactionRepository.save(transaction);

        try {

            BigDecimal fee = dto.amount().subtract(BigDecimal.valueOf(2))
                    .multiply(BigDecimal.valueOf(0.001)).setScale(6, RoundingMode.DOWN);

            BigDecimal withdrawAmount = dto.amount().subtract(fee);

            log.info("트론 네트워크로 출금 시도: Address={}, Amount={}", dto.walletAddress(), withdrawAmount);


            // TronRawService 호출 (실제 USDT 전송)
            String txId = tronRawService.sendUSDT(dto.walletAddress(), withdrawAmount);

            savedTransaction.setTxid(txId);
            savedTransaction.setStatus(Status.COMPLETED);
            savedTransaction.setFee(fee);

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

    // 입/출금 거래 내역 조회
    @Transactional(readOnly = true)
    public Page<ResponseTransactionDto> findTransaction(
            Long userId,
            Integer year,
            Integer month,
            Type type,
            Pageable pageable
    ) {
        // 조회
        Page<Transaction> transactions = transactionRepository.searchTransaction(userId, year, month, type, pageable);

        return transactions.map(transaction -> ResponseTransactionDto.from(transaction));
    }

    // 입금확인
    @Transactional
    public ResponseTransactionDto confirmDeposit(Long transactionId, String txid, Long userId){

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        if (transaction.getStatus() == Status.COMPLETED) {
            return ResponseTransactionDto.from(transaction);
        }

        TransferInfo info = txidService.findInfoByTxid(txid);

        if (!transaction.getExternalWallet().getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.INVALID_USER_ATTEMPT);
        }

        if(info == null){
            transaction.setTxid(txid);
            return ResponseTransactionDto.from(transaction);
        }

        if (!serviceWalletAddress.equalsIgnoreCase(info.to())) {
            throw new CustomException(ErrorCode.TRANSACTION_NOT_FOUND);
        }

        UserWallet userWallet = userWalletRepository.findByUserIdWithLock(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_WALLET_NOT_FOUND));

        BigDecimal newBalance = userWallet.getBalance().add(info.amount());

        userWallet.setBalance(newBalance);
        transaction.setStatus(Status.COMPLETED);
        transaction.setTxid(txid);

        return ResponseTransactionDto.from(transaction);
    }
}
