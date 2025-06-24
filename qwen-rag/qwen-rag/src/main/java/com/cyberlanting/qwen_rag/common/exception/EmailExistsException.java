package com.cyberlanting.qwen_rag.common.exception;

public class EmailExistsException extends BaseException{
    public EmailExistsException() {

    }

    public EmailExistsException(String msg) {
        super(msg);
    }
}
