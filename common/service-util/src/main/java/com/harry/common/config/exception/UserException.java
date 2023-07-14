package com.harry.common.config.exception;

import com.harry.common.result.ResultCodeEnum;

public class UserException extends RuntimeException{
    private Integer code;
    private String msg;

    public UserException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }


    public UserException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "DeleteMenuException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
