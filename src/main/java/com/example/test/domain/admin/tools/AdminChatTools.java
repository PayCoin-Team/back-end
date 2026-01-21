package com.example.test.domain.admin.tools;

import com.example.test.domain.exchange.service.ExchangeRateService;
import com.example.test.domain.transaction.repository.TransactionRepository;
import com.example.test.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ai.tool.annotation.Tool;

import java.math.BigDecimal;


@Component
@RequiredArgsConstructor
public class AdminChatTools {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final ExchangeRateService exchangeRateService;

    @Tool(description = "현재 서비스에 가입된 총 사용자 수를 조회합니다.")
    public long getUserCount() {
        return userRepository.count();
    }

    @Tool(description = "시스템에 기록된 총 거래 건수를 조회합니다.")
    public long getTransactionCount() {
        return transactionRepository.count();
    }

    @Tool(description = "코인(USDT, BTC 등)이나 채권의 현재 실시간 가격(환율)을 조회합니다.")
    public String getExchangeRate(String currencyCode) {
        String code = currencyCode.toUpperCase();

        if (code.equals("USDT")) {
            code = "KRW";
        }

        try {
            BigDecimal rate = exchangeRateService.getRate(code);

            // "KRW"를 조회했다면 그것은 "USDT의 원화 가격"임을 명시합니다.
            if (code.equals("KRW")) {
                return String.format("현재 USDT의 가격은 %s원입니다.", rate.toPlainString());
            }

            return String.format("%s 기준 USDT 가격은 %s입니다.", code, rate.toPlainString());
        } catch (Exception e) {
            return String.format("시스템 DB에서 '%s' 관련 가격 정보를 찾을 수 없습니다. (현재 지원: KRW, USD, JPY, CNY, EUR, VND, GBP)", currencyCode);
        }
    }
}
