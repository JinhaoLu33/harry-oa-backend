package com.harry.common.result;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;//status
    private String message;
    private T data;//actual data

    private Result() {
    }

    //encapsulate return data
    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<>();
        if (body != null) {
            result.setData(body);
        }
        //status
        result.setCode(resultCodeEnum.getCode());
        //message
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    //success
    public static <T> Result<T> success() {
        return build(null, ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return build(data, ResultCodeEnum.SUCCESS);
    }

    //fail
    public static <T> Result<T> fail() {
        return build(null, ResultCodeEnum.FAIL);
    }
}
