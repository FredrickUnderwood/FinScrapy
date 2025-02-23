package com.chen.dto;

import com.chen.entity.FullTextData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzerDTO {

    private String startDate;

    private String endDate;

    private String keyword;

    LinkedHashMap<String, List<FullTextData>> timelineData;
}
