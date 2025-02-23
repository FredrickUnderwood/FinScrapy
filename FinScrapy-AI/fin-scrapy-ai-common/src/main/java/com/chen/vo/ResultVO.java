package com.chen.vo;

import com.chen.enums.RespStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO<T> implements Serializable {

    private String code;

    private String msg;

    private T data;

    public ResultVO(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVO(RespStatusEnum respStatusEnum) {
        this(respStatusEnum.getCode(), respStatusEnum.getMsg());
    }

    public ResultVO(RespStatusEnum respStatusEnum, T data) {
        this(respStatusEnum.getCode(), respStatusEnum.getMsg(), data);
    }

    public static <T> ResultVO<T> success() {
        return new ResultVO<>(RespStatusEnum.SUCCESS);
    }

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<>(RespStatusEnum.SUCCESS, data);
    }

    public static <T> ResultVO<T> error(RespStatusEnum respStatusEnum) {
        return new ResultVO<>(respStatusEnum);
    }

}
