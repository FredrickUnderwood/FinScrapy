package com.chen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendRequest {

    private String code;

    private Long messageTemplateId;

    private MessageParam messageParam;

    private List<String> recallMessageIdList;
}
