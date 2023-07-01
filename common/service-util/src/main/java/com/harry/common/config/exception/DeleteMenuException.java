package com.harry.common.config.exception;

import com.harry.common.result.ResultCodeEnum;

public class DeleteMenuException extends RuntimeException{
    private Integer code;
    private String msg;

    public DeleteMenuException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }


    public DeleteMenuException(ResultCodeEnum resultCodeEnum) {
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
