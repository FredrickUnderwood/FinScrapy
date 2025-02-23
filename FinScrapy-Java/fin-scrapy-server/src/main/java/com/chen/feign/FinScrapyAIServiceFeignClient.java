package com.chen.feign;

import com.chen.config.FinScrapyAIFeignConfig;
import com.chen.dto.AnalyzerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "FinScrapyAI", configuration = FinScrapyAIFeignConfig.class)
public interface FinScrapyAIServiceFeignClient {

    @PostMapping("/api/ai/analyzer/ai_gen_prediction")
    public String genAIPrediction(@RequestBody AnalyzerDTO analyzerDTO);

    @PostMapping("/api/ai/analyzer/ai_gen_generalization")
    public String genAIGeneralization(@RequestBody AnalyzerDTO analyzerDTO);

}
