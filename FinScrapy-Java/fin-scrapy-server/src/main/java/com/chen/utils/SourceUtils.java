package com.chen.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SourceUtils {

    @Value("${fin-scrapy.sources}")
    private String sources;

    @Value("${fin-scrapy.sources_chinese}")
    private String sourcesChinese;

    public Map<String, String> getSourcesMap() {
        Map<String, String> sourcesMap = new HashMap<>();
        List<String> sourcesList = Arrays.asList(sources.split(","));
        List<String> sourcesChineseList = Arrays.asList(sourcesChinese.split(","));
        for (int i = 0; i < sourcesList.size(); i++) {
            sourcesMap.put(sourcesList.get(i), sourcesChineseList.get(i));
        }
        return sourcesMap;
    }

}
