package com.cyberlanting.qwen_rag.pojo.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String avatarUrl;
    private String captcha;
}
