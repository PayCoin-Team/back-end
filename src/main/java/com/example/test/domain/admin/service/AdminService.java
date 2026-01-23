package com.example.test.domain.admin.service;

import com.example.test.domain.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final TransactionRepository transactionRepository;
}
