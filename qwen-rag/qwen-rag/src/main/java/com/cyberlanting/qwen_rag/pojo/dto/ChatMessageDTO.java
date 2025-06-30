package com.cyberlanting.qwen_rag.pojo.dto;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO implements ChatMessage {

    private ChatMessageType type;
    private String text;

    public ChatMessageType type() {
        return ChatMessageType.AI;
    }
}
