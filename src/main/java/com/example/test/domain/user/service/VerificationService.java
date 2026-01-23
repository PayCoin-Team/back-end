package com.example.test.domain.user.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationService {
    // 이메일, 인증번호
    private final Map<String, String> codes = new ConcurrentHashMap<>();
    // 이메일, 인증완료여부
    private final Map<String, Boolean> verifiedStatus = new ConcurrentHashMap<>();


    public void saveCode(String email, String code) {
        codes.put(email, code);
        verifiedStatus.put(email, false);
    }

    public boolean verifyCode(String email, String code) {
        if (code.equals(codes.get(email))) {
            verifiedStatus.put(email, true); // 인증 성공 상태로 변경
            return true;
        }
        return false;
    }

    public boolean isVerified(String email) {
        return verifiedStatus.getOrDefault(email, false);
    }

    public void clear(String email) {
        codes.remove(email);
        verifiedStatus.remove(email);
    }
}
