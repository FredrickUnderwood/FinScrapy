package com.chen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryFullTextDataDTO {

    private String source;

    private String startTime;

    private String endTime;

    private String keywords;

    private Integer size;

    private Integer page;
}
