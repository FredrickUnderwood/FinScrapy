package com.chen.feign;

import com.chen.dto.AnalyzerDTO;
import org.springframework.stereotype.Component;

@Component
public class FinScrapyAIServiceFallback implements FinScrapyAIServiceFeignClient{
    @Override
    public String genAIPrediction(AnalyzerDTO analyzerDTO) {
        return "Generate AI Prediction failed!";
    }

    @Override
    public String genAIGeneralization(AnalyzerDTO analyzerDTO) {
        return "Generate AI Generalization failed!";
    }
}
