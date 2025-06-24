package com.cyberlanting.qwen_rag.common.exception;

public class UsernameExistsException extends BaseException{

    public UsernameExistsException() {

    }

    public UsernameExistsException(String msg) {
        super(msg);
    }
}
