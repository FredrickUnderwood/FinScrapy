package com.chen.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RespStatusEnum {
    ERROR_500("500", "Internal Server Error"),
    ERROR_400("400", "Bad Request"),
    SUCCESS("200", "Success"),
    SUBSCRIPTION_USER_ILLEGAL("SUBSCRIPTION_001", "用户未找到或用户被禁用"),
    SUBSCRIPTION_TOO_MANY("SUBSCRIPTION_002", "订阅数过多，请升级后订阅或关闭现有订阅"),
    SUBSCRIPTION_NOTIFY_ILLEGAL("SUBSCRIPTION_003", "推送类别未解锁，请升级后解锁"),
    SUBSCRIPTION_DUPLICATE_KEYWORDS("SUBSCRIPTION_004", "订阅关键词重复"),
    USER_CREATE_INFO_ILLEGAL("USER_001", "用户注册信息不合法"),
    USER_NOT_FOUND("USER_002", "用户不存在"),
    USER_PASSWORD_WRONG("USER_003", "密码错误"),
    USER_ILLEGAL("USER_004", "用户被禁用")
    ;


    /**
     * 响应状态
     */
    private final String code;
    /**
     * 响应编码
     */
    private final String msg;

    }
