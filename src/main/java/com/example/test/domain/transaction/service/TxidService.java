package com.example.test.domain.transaction.service;

import com.example.test.domain.polling.config.TronProperties;
import com.example.test.domain.transaction.dto.TronTxidResponse;
import com.example.test.domain.transaction.entity.Status;
import com.example.test.domain.transaction.entity.Transaction;
import com.example.test.domain.transaction.repository.TransactionRepository;
import com.example.test.global.exception.CustomException;
import com.example.test.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.tron.trident.utils.Base58Check;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TxidService {
    private final TronProperties properties;
    private final TransactionRepository transactionRepository;
    private final @Qualifier("tronGridWebClient") WebClient tronGridClient;

    @Transactional
    public Status findInfoByTxid(Long transactionId, String txid) {

        TronTxidResponse tron;

        try {
            tron = tronGridClient.post()
                    .uri("/wallet/gettransactioninfobyid")
                    .bodyValue(Map.of("value", txid))
                    .retrieve()
                    .bodyToMono(TronTxidResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new CustomException(ErrorCode.TRON_API_ERROR);
        }

        Transaction ts = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));

        if (ts.getStatus() == Status.COMPLETED || ts.getStatus() == Status.FAILED)
            return ts.getStatus();


        // 아직 처리 안됐으면 PENDING 리턴
        if (isPending(tron)) {
            ts.setTxid(txid);
            ts.setStatus(Status.PENDING);
            return Status.PENDING;
        }

        // 실패로 오면 FAILED 리턴
        if (!isSuccess(tron)) {
            ts.setTxid(txid);
            ts.setStatus(Status.FAILED);
            return Status.FAILED;
        }

        ts.setStatus(Status.COMPLETED);
        return Status.COMPLETED;
    }

    public boolean isPending(TronTxidResponse resp) {
        if (resp == null) return true;
        if (resp.blockNumber() == null) return true;
        if (resp.receipt() == null) return true;
        return resp.receipt().result() == null || resp.receipt().result().isBlank();
    }

    public boolean isSuccess(TronTxidResponse resp) {
        return resp != null
                && resp.receipt() != null
                && "SUCCESS".equalsIgnoreCase(resp.receipt().result());
    }

    // hex 주소를 Base58로 변환하는 함수, tron에서 운영하는 trident 라이브러리 사용
    private String convertHexToBase58(String hexAddress) {
        // 만약 64자리라면 앞의 24자리(0)를 떼어내고 41을 붙임
        if (hexAddress.length() == 64) {
            hexAddress = "41" + hexAddress.substring(24);
        }

        // 41이 없는 경우 41을 붙임
        if (hexAddress.length() == 40) {
            hexAddress = "41" + hexAddress;
        }

        // 변환 로직
        byte[] raw = Hex.decode(hexAddress);
        return Base58Check.bytesToBase58(raw);
    }
}
