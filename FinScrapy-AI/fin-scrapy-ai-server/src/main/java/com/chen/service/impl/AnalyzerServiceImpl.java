package com.chen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chen.client.DeepSeekV3Client;
import com.chen.dto.AnalyzerDTO;
import com.chen.service.AnalyzerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Scanner;

@Service
@Slf4j
public class AnalyzerServiceImpl implements AnalyzerService {

    private static final String AI_ANALYZER_PREDICTION_PROMPT_TEMPLATE_PATH = "prompt_template/ai_analyzer_prediction_prompt_template.txt";

    private static final String AI_ANALYZER_GENERALIZATION_PROMPT_TEMPLATE_PATH = "prompt_template/ai_analyzer_generalization_prompt_template.txt";

    private static final String LOG_PREFIX = "[AnalyzerService]";

    @Autowired
    private DeepSeekV3Client deepSeekV3Client;


    @Override
    public String genAIAnalysis(AnalyzerDTO analyzerDTO, String analysisType) {
        StringBuilder promptBuilder = new StringBuilder();
        String filePath;
        if (analysisType.equals("prediction")) {
            filePath = AI_ANALYZER_PREDICTION_PROMPT_TEMPLATE_PATH;
        } else {
            filePath = AI_ANALYZER_GENERALIZATION_PROMPT_TEMPLATE_PATH;
        }
        InputStream inputStream = AnalyzerService.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            log.error("{}Read prompt template failed, file: {} not found", LOG_PREFIX, filePath);
            return "";
        }
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                promptBuilder.append(scanner.nextLine()).append("\n");
            }
        }
        String promptTemplate  = promptBuilder.toString().replace("{关键词}", analyzerDTO.getKeyword())
                .replace("{起始日期}", analyzerDTO.getStartDate())
                .replace("{截止日期}", analyzerDTO.getEndDate())
                .replace("{时间线}", analyzerDTO.getTimelineData().toString());
        String responseBody = deepSeekV3Client.doPost(promptTemplate);
        JSONObject jsonResponse = JSONObject.parseObject(responseBody);
        return jsonResponse.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }
}
