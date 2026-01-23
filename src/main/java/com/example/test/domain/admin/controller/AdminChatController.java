package com.example.test.domain.admin.controller;

import com.example.test.domain.admin.controller.api.AdminChatApi;
import com.example.test.domain.admin.service.AdminChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/chat")
@RequiredArgsConstructor
public class AdminChatController implements AdminChatApi {

    private final AdminChatService adminChatService;

    @Override
    @PostMapping
    public String ask(@RequestBody String message) {
        return adminChatService.chat(message);
    }
}