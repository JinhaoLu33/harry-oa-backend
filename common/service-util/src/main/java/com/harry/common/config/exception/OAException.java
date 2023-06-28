package com.harry.common.config.exception;

import com.harry.common.result.ResultCodeEnum;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class OAException extends RuntimeException {
    private Integer code;
    private String msg;

    public OAException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }


    public OAException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
