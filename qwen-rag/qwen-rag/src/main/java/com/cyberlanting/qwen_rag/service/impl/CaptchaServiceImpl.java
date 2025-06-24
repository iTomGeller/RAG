package com.cyberlanting.qwen_rag.service.impl;


import com.cyberlanting.qwen_rag.service.CaptchaService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 生成验证码
     * @param email 目标邮箱
     * @return 验证码
     */
    public String generateCaptcha(String email) {
        String code = RandomStringUtils.randomNumeric(6);
        redisTemplate.opsForValue().set(
                "QWENRAG:CAPTCHA:" + email,
                code,
                Duration.ofMinutes(5)  // 5分钟有效期
        );
        return code;
    }

    /**
     * 验证码校验
     * @param email 邮箱
     * @param code 验证码
     * @return 是否验证通过
     */
    public boolean validateCaptcha(String email, String code) {
        String captcha = redisTemplate.opsForValue().get("QWENRAG:CAPTCHA:" + email);
        return captcha != null && captcha.equals(code);
    }

}
