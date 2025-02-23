package com.chen.controller;

import com.chen.dto.QueryFullTextDataDTO;
import com.chen.service.AnalyzerService;
import com.chen.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analyzer")
public class AnalyzerController {

    @Autowired
    private AnalyzerService analyzerService;

    @PostMapping("/prediction")
    public ResultVO<String> predict(@RequestBody QueryFullTextDataDTO queryFullTextDataDTO) {
        return ResultVO.success(analyzerService.getAIAnalysis(queryFullTextDataDTO, "prediction"));
    }

    @PostMapping("/generalization")
    public ResultVO<String> generalize(@RequestBody QueryFullTextDataDTO queryFullTextDataDTO) {
        return ResultVO.success(analyzerService.getAIAnalysis(queryFullTextDataDTO, "generalization"));
    }
}
