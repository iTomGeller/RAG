package com.cyberlanting.qwen_rag.controller;

import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.entity.Chat;
import com.cyberlanting.qwen_rag.service.Assistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private Assistant assistant;

    @PostMapping(produces = "text/html;charset=utf-8")
    public Flux<String> chat(String memoryId, String message) {
        Flux<String> result = assistant.chat(memoryId, message);
        return result;
    }

    public Result<Chat> getChatList() {
        return null;
    }

}
