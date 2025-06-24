package com.cyberlanting.qwen_rag.common.exception;

public class BaseException extends RuntimeException{

    BaseException() { }

    BaseException(String msg) {
        super(msg);
    }
}
