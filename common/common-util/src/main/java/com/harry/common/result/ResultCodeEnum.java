package com.harry.common.result;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;

import javax.annotation.Generated;
import lombok.Getter;

@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "success"),
    FAIL(201, "fail"),
    SERVICE_ERROR(2012, "service error"),
    DATA_ERROR(204, "data error"),

    LOGIN_AUTH(208, "not login"),
    PERMISSION(209, "no permission");

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
