package com.back.chatbot.controller;

import com.back.chatbot.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
public class ChatBotController {

    private final ChatBotService ChatBotService;

    @PostMapping("/message")
    public ResponseEntity<ChatBotService.BotResponse> handleMessage(@RequestBody String input) throws Exception {
        return ResponseEntity.ok(ChatBotService.processMessage(input));
    }
}