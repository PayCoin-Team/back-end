package com.example.test.domain.admin.service;

import com.example.test.domain.admin.entity.Difference;
import com.example.test.domain.admin.entity.Verification;
import com.example.test.domain.admin.repository.VerificationRepository;
import com.example.test.domain.polling.service.PollingService;
import com.example.test.domain.transaction.enums.Status;
import com.example.test.domain.transaction.repository.TransactionRepository;
import com.example.test.domain.userwallet.repository.UserWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class VerifyScheduler {

    private final PollingService pollingService;
    private final TransactionRepository transactionRepository;
    private final UserWalletRepository userWalletRepository;
    private final VerificationRepository verificationRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void verifyBalance(){
        BigDecimal serverBalance = pollingService.getUsdtBalance().usdtBalance();
        BigDecimal totalFees = transactionRepository.sumFees(Status.COMPLETED);
        BigDecimal totalServiceWallet = userWalletRepository.sumBalances();

        Difference difference;

        if(totalFees.add(totalServiceWallet).compareTo(serverBalance) != 0)
            difference = Difference.DIFFER;
        else difference = Difference.EQUAL;

        Verification verification = Verification.builder().serverBalance(serverBalance)
                .userBalance(totalServiceWallet)
                .totalFee(totalFees)
                .difference(difference)
                .build();

        verificationRepository.save(verification);
    }
}
