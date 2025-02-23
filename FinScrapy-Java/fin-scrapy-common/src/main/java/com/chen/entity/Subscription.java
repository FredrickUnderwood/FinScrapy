package com.chen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {

    private Long id;

    private Long userId;

    private String keywords;

    /*
    0 禁用 1 启用
     */
    private Integer pushNotify;

    /*
    0 禁用 1 启用
     */
    private Integer emailNotify;

    /*
    0 禁用 1 启用
     */
    private Integer status;

    private LocalDateTime createdTime;
}
