package com.atguigu.cloud.resp;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ReturnCodeEnum {

    SUCCESS("200", "操作成功"),
    FAIL("500", "操作失败"),
    UNAUTHORIZED("401", "暂未登录或token已经过期"),
    FORBIDDEN("403", "没有相关权限");

    private final String code;
    private final String message;

    ReturnCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public static ReturnCodeEnum getReturnCodeEnum(String code) {
        return Arrays.stream(ReturnCodeEnum.values()).filter(resp -> resp.getCode().equals(code)).findFirst().orElse(null);
    }

}
