package com.example.test.domain.polling.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TronGridResponse(
        List<TronTransfer> data,
        TronMeta meta
) {
}
