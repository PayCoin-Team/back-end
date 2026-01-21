package com.example.test.domain.admin.service;

import com.example.test.domain.admin.tools.AdminChatTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AdminChatService {

    private final ChatClient chatClient;

    public AdminChatService(ChatClient.Builder builder, AdminChatTools adminTools) {
        this.chatClient = builder
                .defaultSystem("""
                당신은 CrossPay 관리 비서입니다.\s
                                사용자가 '테더'나 'Tether'를 물어보면 'USDT' 코드로 변환하여 가격을 조회하세요.
                                코인 가격 조회 시 KRW(원화) 기준 가격을 안내합니다.
            
            [수행 업무]
            1. 내부 데이터: 사용자 수, 거래 건수를 조회합니다.
            2. 코인 가격: 코인 가격 문의 시 반드시 'getCoinPrice' 도구를 사용하여 업비트 실시간 시세를 제공하세요.
            
            [규칙]
            - 금융 및 서비스 운영과 관련 없는 질문은 "CrossPay 관련 업무만 지원합니다."라고 정중히 거절하세요.
            - 가격 정보 뒤에는 반드시 "(Upbit 기준)"이라는 출처를 명시하세요.
            """)
                .defaultTools(adminTools)
                .build();
    }

    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
