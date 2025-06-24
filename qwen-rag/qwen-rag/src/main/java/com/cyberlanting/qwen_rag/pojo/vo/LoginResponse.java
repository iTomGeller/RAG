package com.cyberlanting.qwen_rag.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    UserVO userVO;
    String token;
}
