package com.example.test.domain.transaction.service;

import org.web3j.crypto.Credentials;
import org.web3j.utils.Numeric;
import org.tron.trident.utils.Base58Check; // Trident 라이브러리의 정확한 경로

public class KeyTester {
    public static void main(String[] args) {
        // application-secret.yml에 넣으신 프라이빗 키를 입력하세요
        String privateKey = "73f92ece19e00e26f2cc92600c7e30924960082ae5958a704d1076ef6e86b557";

        try {
            Credentials credentials = Credentials.create(privateKey);
            // 1. 이더리움 형식 주소에서 0x를 떼고 앞에 트론 식별자 41을 붙임
            String hexAddress = "41" + credentials.getAddress().substring(2);

            // 2. Hex 문자열을 바이트 배열로 변환
            byte[] raw = Numeric.hexStringToByteArray(hexAddress);

            // 3. Trident 라이브러리를 사용하여 Base58(T로 시작하는 주소)로 변환
            String base58Address = Base58Check.bytesToBase58(raw);

            System.out.println("====================================");
            System.out.println("프라이빗 키의 실제 트론 주소: " + base58Address);
            System.out.println("====================================");
        } catch (Exception e) {
            System.out.println("에러 발생: 프라이빗 키 형식이 올바르지 않습니다.");
        }
    }
}