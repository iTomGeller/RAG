package com.cyberlanting.qwen_rag.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",
        streamingChatModel = "openAiStreamingChatModel",
        chatMemoryProvider = "chatMemoryProvider"
)
public interface Assistant {

//    @SystemMessage("""
//    请根据用户提供的多条内容（{{ userFeedback }}）执行智能过滤和学习：
//
//    【反馈识别规则】
//    1. 有效反馈特征：
//       - 包含明确评价词汇（如"不准确"、"建议"、"改进"、"更好"等）
//       - 提及回答质量（如"解释不清楚"、"例子太少"）
//       - 有具体修正建议（如"应该补充XX信息"）
//
//    2. 非反馈内容特征：
//       - 普通问题/请求
//       - 闲聊内容
//       - 系统指令
//       - 无评价性语句
//
//    【处理流程】
//    1. 对每条内容进行意图分类（反馈/非反馈）
//    2. 仅提取符合反馈特征的内容
//    3. 将有效反馈转化为改进项（示例）：
//       - "回答太简短" → 增加解释细节
//       - "例子不相关" → 优化案例匹配度
//    4. 完全忽略非反馈内容
//
//    【输出要求】
//    最终只应用识别出的有效反馈，保持对其他内容的自然响应
//    """)
    @SystemMessage("{{ userFeedback }}")
    Flux<String> chat(@MemoryId String memoryId, @UserMessage String message, @V("userFeedback") String userFeedback);

    Flux<String> chat(@MemoryId String memoryId, @UserMessage String message);
}

