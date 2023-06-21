package com.harry.common.config.exception;

import com.harry.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    //global exception
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result error(){
        return Result.fail();
    }
}
