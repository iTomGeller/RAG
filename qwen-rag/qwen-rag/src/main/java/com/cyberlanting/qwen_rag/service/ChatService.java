package com.cyberlanting.qwen_rag.service;

import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.entity.Chat;
import com.cyberlanting.qwen_rag.pojo.entity.DocumentInfo;
import com.cyberlanting.qwen_rag.pojo.vo.DocumentInfoVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import dev.langchain4j.data.message.ChatMessage;

import java.util.List;

public interface ChatService {
    Result<List<Chat>> getChatList();

    List<DocumentInfoVO> queryAndEnhancedPrompt(String message) throws JsonProcessingException;

    Result getChatContext(Long memoryId);

    Result deleteChat(Long memoryId);
}
