package com.cyberlanting.qwen_rag.common.util;

import cn.hutool.core.util.StrUtil;

public class RegexUtils {
    /**
     * 是否是无效手机格式
     * @param phone 要校验的手机号
     * @return true:有效，false：无效
     */
    public static boolean isPhoneValid(String phone){
        return match(phone, RegexPatterns.PHONE_REGEX);
    }
    /**
     * 是否是无效邮箱格式
     * @param email 要校验的邮箱
     * @return true:有效，false：无效
     */
    public static boolean isEmailValid(String email){
        return match(email, RegexPatterns.EMAIL_REGEX);
    }

    /**
     * 是否是无效验证码格式
     * @param code 要校验的验证码
     * @return true:有效，false：无效
     */
    public static boolean isCodeValid(String code){
        return match(code, RegexPatterns.VERIFY_CODE_REGEX);
    }

    public static boolean isPasswordValid(String password){ return match(password, RegexPatterns.PASSWORD_REGEX); }

    // 校验是否不符合正则格式
    private static boolean match(String str, String regex){
        if (StrUtil.isBlank(str)) {
            return false;
        }
        return str.matches(regex);
    }
}
