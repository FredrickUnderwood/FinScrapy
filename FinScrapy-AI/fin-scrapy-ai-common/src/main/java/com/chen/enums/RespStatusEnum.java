package com.chen.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RespStatusEnum {
    ERROR_500("500", "Internal Server Error"),
    ERROR_400("400", "Bad Request"),
    SUCCESS("200", "Success"),
    KEYWORD_AI_GEN_ERROR("KEYWORD_001", "AI生成关键词失败")
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
