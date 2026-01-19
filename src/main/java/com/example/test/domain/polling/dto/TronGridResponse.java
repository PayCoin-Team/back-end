package com.example.test.domain.polling.dto;

import java.util.Map;
import java.util.List;

// TronGrid의 api 결과를 담는 DTO
public record TronGridResponse(List<TronEventData> data,
                               boolean success,
                               Map<String, Object> meta) {
}
