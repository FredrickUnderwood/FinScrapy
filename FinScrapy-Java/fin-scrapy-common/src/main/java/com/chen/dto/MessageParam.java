package com.chen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageParam {

    private String bizId;

    private String receivers;

    private Map<String, String> variables;

    private Map<String, String> extras;
}
