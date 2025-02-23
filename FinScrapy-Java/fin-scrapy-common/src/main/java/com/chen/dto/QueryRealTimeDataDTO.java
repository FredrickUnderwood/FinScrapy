package com.chen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryRealTimeDataDTO {

    private String source;

    private Integer size;

    private Long endTimestamp;

}
