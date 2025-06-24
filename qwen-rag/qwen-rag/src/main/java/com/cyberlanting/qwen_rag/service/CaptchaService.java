package com.cyberlanting.qwen_rag.service;

public interface CaptchaService {
    String generateCaptcha(String email);

    boolean validateCaptcha(String email, String code);
}
