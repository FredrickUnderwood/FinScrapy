package com.chen.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.chen.client.DeepSeekV3Client;
import com.chen.service.KeywordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Service
public class KeywordServiceImpl implements KeywordService {

    private static final String AI_GEN_KEYWORDS_PROMPT_TEMPLATE_PATH = "prompt_template/ai_gen_keywords_prompt_template.txt";

    private static final String LOG_PREFIX = "[KeywordService]";

    private static final Integer MAXIMUM_KEYWORDS_SIZE = 4;

    @Autowired
    private DeepSeekV3Client deepSeekV3Client;


    @Override
    public List<String> getAIGenKeywords(String rootKeyword) {
        StringBuilder promptBuilder = new StringBuilder();
        InputStream inputStream = KeywordService.class.getClassLoader().getResourceAsStream(AI_GEN_KEYWORDS_PROMPT_TEMPLATE_PATH);
        if (inputStream == null) {
            log.error("{}Read prompt template failed, file: {} not found", LOG_PREFIX, AI_GEN_KEYWORDS_PROMPT_TEMPLATE_PATH);
            return new ArrayList<>();
        }
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                promptBuilder.append(scanner.nextLine()).append("\n");
            }
        }
        String prompt = promptBuilder.toString().replace("%s", rootKeyword);
        String responseBody = deepSeekV3Client.doPost(prompt);
        JSONObject jsonResponse = JSONObject.parseObject(responseBody);
        String content = jsonResponse.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
        List<String> keywords = new ArrayList<>();
        keywords.add(rootKeyword);
        for (String keyword : content.split(",")) {
            keywords.add(keyword.trim());
        }
        return keywords.subList(0, MAXIMUM_KEYWORDS_SIZE);
    }
}
