package com.chen.service;

import com.chen.dto.AnalyzerDTO;

public interface AnalyzerService {

    String genAIAnalysis(AnalyzerDTO analyzerDTO, String analysisType);
}
