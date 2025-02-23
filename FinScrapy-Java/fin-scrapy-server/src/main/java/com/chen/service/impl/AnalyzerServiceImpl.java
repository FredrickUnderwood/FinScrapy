package com.chen.service.impl;

import com.chen.analyzer.FullTextAnalyzer;
import com.chen.dto.AnalyzerDTO;
import com.chen.dto.QueryFullTextDataDTO;
import com.chen.entity.FullTextData;
import com.chen.feign.FinScrapyAIServiceFeignClient;
import com.chen.service.AnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class AnalyzerServiceImpl implements AnalyzerService {

    @Autowired
    private FullTextAnalyzer fullTextAnalyzer;

    @Autowired
    private FinScrapyAIServiceFeignClient finScrapyAIServiceFeignClient;


    @Override
    public String getAIAnalysis(QueryFullTextDataDTO queryFullTextDataDTO, String analysisType) {
        List<String> keywords = List.of(queryFullTextDataDTO.getKeywords().split(","));
        String startDate = queryFullTextDataDTO.getStartTime().substring(0, 10);
        String endDate = queryFullTextDataDTO.getEndTime().substring(0, 10);
        StringBuilder analysisResult = new StringBuilder();
        for (String keyword: keywords) {
            queryFullTextDataDTO.setKeywords(keyword);
            LinkedHashMap<String, List<FullTextData>> result = fullTextAnalyzer.buildFullTextTimeline(queryFullTextDataDTO);
            AnalyzerDTO analyzerDTO = new AnalyzerDTO(startDate, endDate, keyword, result);
            if (analysisType.equals("prediction")) {
                analysisResult.append(finScrapyAIServiceFeignClient.genAIPrediction(analyzerDTO));
            } else {
                analysisResult.append(finScrapyAIServiceFeignClient.genAIGeneralization(analyzerDTO));
            }

        }
        return analysisResult.toString();
    }

    // TODO 存入Redis逻辑
}
