package com.cyberlanting.qwen_rag.handler;

import com.cyberlanting.qwen_rag.common.exception.BaseException;
import com.cyberlanting.qwen_rag.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理SQL异常：重复插入
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        //样例报错：Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '1-2' for key 'PRIMARY'
        //; Duplicate entry '1-2' for key 'PRIMARY'] with root cause
        String message = ex.getMessage();
        if (message.contains("Duplicate entry")) {
//            String[] split = message.split(" ");
//            String relation = split[2];
//            String msg = relation + "之间的好友申请已发送";
            return Result.error("重复操作");
        } else {
            return Result.error("遇到此信息记得截图并且记录报错时的使用场景" + ex.getMessage());
        }
    }
}
