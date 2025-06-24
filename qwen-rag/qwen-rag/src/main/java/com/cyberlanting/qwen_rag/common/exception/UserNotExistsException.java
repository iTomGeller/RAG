package com.cyberlanting.qwen_rag.common.exception;

public class UserNotExistsException extends BaseException{
    public UserNotExistsException(){

    }

    public UserNotExistsException(String msg) {
        super(msg);
    }
}
