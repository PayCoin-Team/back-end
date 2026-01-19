package com.example.test.domain.polling.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tron")
public record TronProperties(Trongrid trongrid,
                             Token token,
                             Wallet wallet,
                             Polling polling) {
    public record Trongrid(String baseUrl, String apiKey, long timeoutMs) {}

    public record Token(String usdtContract) {}

    public record Wallet(String serverAddress) {}

    public record Polling(long intervalMs, int batchSize) {}
}
