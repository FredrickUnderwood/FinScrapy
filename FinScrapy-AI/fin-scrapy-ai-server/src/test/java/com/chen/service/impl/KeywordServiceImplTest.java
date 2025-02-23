package com.chen.service.impl;


import com.chen.service.KeywordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KeywordServiceImplTest {

    @Autowired
    private KeywordService keywordService;

    @Test
    void getAIGenKeywords() {
        System.out.println(keywordService.getAIGenKeywords("英伟达"));

    }
}