package com.chen.controller;

import com.chen.dto.QueryFullTextDataDTO;
import com.chen.entity.FullTextData;
import com.chen.service.FullTextService;
import com.chen.vo.FullTextDataVO;
import com.chen.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/full_text")
public class FullTextController {

    @Autowired
    private FullTextService fullTextService;

    @PostMapping("/query")
    public ResultVO<List<FullTextData>> queryFullTextSpecifiedSource(@RequestBody QueryFullTextDataDTO queryFullTextDataDTO) {
        return ResultVO.success(fullTextService.queryFullTextSpecifiedSource(queryFullTextDataDTO));
    }

}
