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
public class User {

    private Long id;

    private String username;

    private String password;

    private Integer level;

    private String email;

    private String phone;

    private LocalDateTime createdTime;

    /*
    0 禁用 1 启用
     */
    private Integer status;

}
