package com.chen.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.chen.constant.FinScrapyConstant;
import com.chen.dto.QueryFullTextDataDTO;
import com.chen.elasticsearch.FullTextESMapper;
import com.chen.entity.FullTextData;
import com.chen.service.FullTextService;
import com.chen.vo.FullTextDataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FullTextServiceImpl implements FullTextService {

    private static final String LOG_PREFIX = "[FullTextService]";

    @Autowired
    private FullTextESMapper fullTextESMapper;


    @Override
    public List<FullTextData> queryFullTextSpecifiedSource(QueryFullTextDataDTO queryFullTextDataDTO) {
        queryFullTextDataDTO.setSize(Math.min(queryFullTextDataDTO.getSize(), FinScrapyConstant.MAXIMUM_FULL_TEXT_DATA_PAGE_SIZE));
        return fullTextESMapper.searchFullTextDataForServer(queryFullTextDataDTO, LOG_PREFIX);
    }
}
