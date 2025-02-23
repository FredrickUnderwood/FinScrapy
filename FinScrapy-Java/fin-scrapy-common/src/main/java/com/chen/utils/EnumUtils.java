package com.chen.utils;

import com.chen.enums.FinScrapyEnum;

import java.util.ArrayList;
import java.util.List;

public class EnumUtils {

    public static <E extends FinScrapyEnum> E getEnumByCode(Integer code, Class<E> clazz) {
        for (E e : clazz.getEnumConstants()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static <E extends FinScrapyEnum> List<Integer> getCodeList(Class<E> clazz) {
        List<Integer> codeList = new ArrayList<>();
        for (E e : clazz.getEnumConstants()) {
            codeList.add(e.getCode());
        }
        return codeList;
    }

}
