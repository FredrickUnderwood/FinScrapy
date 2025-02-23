package com.chen.controller;

import com.chen.enums.RespStatusEnum;
import com.chen.service.KeywordService;
import com.chen.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/ai/keyword")
public class KeywordController {

    @Autowired
    private KeywordService keywordService;

    @GetMapping("/ai_gen_keywords")
    public ResultVO<List<String>> getAIGenKeywords(@RequestParam("rootKeyword") String rootKeyword) {
        List<String> result = keywordService.getAIGenKeywords(rootKeyword);
        if (Objects.nonNull(result) && !result.isEmpty()) {
            return ResultVO.success(result);
        }
        return ResultVO.error(RespStatusEnum.KEYWORD_AI_GEN_ERROR);


    }
}
