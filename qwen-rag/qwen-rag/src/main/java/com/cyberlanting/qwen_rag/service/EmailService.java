package com.cyberlanting.qwen_rag.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendCaptchaEmail(String email, String authCode) throws MessagingException;
}
