package com.cyberlanting.qwen_rag.controller;

import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.entity.Chat;
import com.cyberlanting.qwen_rag.service.Assistant;
import com.cyberlanting.qwen_rag.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private Assistant assistant;

    @Autowired
    private ChatService chatService;

    @PostMapping(produces = "text/html;charset=utf-8")
    public Flux<String> chat(String memoryId, String message) throws JsonProcessingException {
        message = chatService.queryAndEnhancedPrompt(message);
        Flux<String> result = assistant.chat(memoryId, message);
        return result;
    }

    @GetMapping("/list")
    public Result<List<Chat>> getChatList() {
        return chatService.getChatList();
    }

}
