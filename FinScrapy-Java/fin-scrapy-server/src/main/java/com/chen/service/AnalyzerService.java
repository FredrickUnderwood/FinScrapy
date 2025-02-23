package com.chen.service;

import com.chen.dto.QueryFullTextDataDTO;

public interface AnalyzerService {

    String getAIAnalysis(QueryFullTextDataDTO queryFullTextDataDTO, String analysisType);

}
