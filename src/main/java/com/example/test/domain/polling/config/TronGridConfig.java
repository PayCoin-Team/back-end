package com.example.test.domain.polling.config;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class TronGridConfig {
    @Bean
    public WebClient tronGridWebClient(TronProperties properties){
        TronProperties.Trongrid trongrid = properties.trongrid();

        // 응답 타임아웃 설정
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofSeconds(10));

        WebClient.Builder tron = WebClient.builder().baseUrl(trongrid.baseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        if(trongrid.apiKey() != null && !trongrid.apiKey().isBlank())
            tron.defaultHeader("TRON-PRO-API-KEY", trongrid.apiKey());

        return tron.build();
    }
}
