package com.chen.controller;

import com.chen.dto.AnalyzerDTO;
import com.chen.service.AnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/analyzer")
public class AnalyzerController {

    @Autowired
    private AnalyzerService analyzerService;

    @PostMapping("/ai_gen_prediction")
    public String genAIPrediction(@RequestBody AnalyzerDTO analyzerDTO) {
        return analyzerService.genAIAnalysis(analyzerDTO, "prediction");
    }

    @PostMapping("/ai_gen_generalization")
    public String genAIGeneralization(@RequestBody AnalyzerDTO analyzerDTO) {
        return analyzerService.genAIAnalysis(analyzerDTO, "generalization");
    }

}
