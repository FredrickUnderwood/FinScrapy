package com.chen.analyzer;

import com.chen.dto.AnalyzerDTO;
import com.chen.dto.QueryFullTextDataDTO;
import com.chen.elasticsearch.FullTextESMapper;
import com.chen.entity.FullTextData;
import com.chen.redis.FullTextAnalyzerRedisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Component
public class FullTextAnalyzer {

    private static final String LOG_PREFIX = "[FullTextAnalyzer]";

    @Autowired
    private FullTextESMapper fullTextESMapper;

    @Autowired
    private FullTextAnalyzerRedisMapper fullTextAnalyzerRedisMapper;

    /**
     * 全量数据查询，不可暴露给外部
     * keywords只传入一个keyword进行查询
     * 每次查询完全量数据保存到Redis
     * @param queryFullTextDataDTO
     * @return
     */
    public LinkedHashMap<String, List<FullTextData>> buildFullTextTimeline(QueryFullTextDataDTO queryFullTextDataDTO) {
        LinkedHashMap<String, List<FullTextData>> timelineMap = new LinkedHashMap<>();
        fullTextESMapper.searchFullTextDataForAnalyzer(queryFullTextDataDTO, LOG_PREFIX).forEach(fullTextDataVO -> {
            String timestamp = fullTextDataVO.getFullTextData().getTimestamp();
            String date = timestamp.substring(0, 10);
            timelineMap.computeIfAbsent(date, d -> new ArrayList<>())
                    .add(fullTextDataVO.getFullTextData());
        });
        // TODO 存入Redis逻辑
//        fullTextAnalyzerRedisMapper.storeFullTextDataToRedis(queryFullTextDataDTO.getKeywords(), timelineMap);
        return timelineMap;
    }
}
