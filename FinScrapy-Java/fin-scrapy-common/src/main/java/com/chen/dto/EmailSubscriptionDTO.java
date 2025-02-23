package com.chen.dto;

import com.chen.entity.RealTimeData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailSubscriptionDTO {

    private Set<String> emails;

    private RealTimeData realTimeData;

    private String keyword;

    private String source;
}
