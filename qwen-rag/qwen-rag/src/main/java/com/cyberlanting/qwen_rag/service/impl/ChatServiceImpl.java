package com.cyberlanting.qwen_rag.service.impl;

import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.pojo.entity.Chat;
import com.cyberlanting.qwen_rag.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Result<List<Chat>> getChatList() {
        Set<String> keys = new HashSet<>();
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(ScanOptions.scanOptions().match("OTTAR:MEMORY:*").build());
        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));
        }

        List<Chat> chatList = new ArrayList<>();

        for (String key : keys) {
            // 提取 memoryId
            String memoryIdStr = key.replace("OTTAR:MEMORY:", "");
            Long memoryId = Long.valueOf(memoryIdStr);

            // 构造 Chat 对象，name 可能需要额外处理，比如从数据库或其他缓存中查
            Chat chat = new Chat();
            chat.setMemoryId(memoryId);
            chat.setName("会话" + memoryId); // 示例默认名，可根据业务逻辑替换
            chatList.add(chat);
        }

        return Result.success(chatList); // 使用你自己的 Result 工具类
    }
}
