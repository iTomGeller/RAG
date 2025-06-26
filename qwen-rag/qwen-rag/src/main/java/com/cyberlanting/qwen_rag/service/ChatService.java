package com.cyberlanting.qwen_rag.service;

import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.entity.Chat;

import java.util.List;

public interface ChatService {
    Result<List<Chat>> getChatList();
}
