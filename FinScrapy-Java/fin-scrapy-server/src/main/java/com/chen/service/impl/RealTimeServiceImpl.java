package com.chen.service.impl;

import com.alibaba.fastjson.JSON;
import com.chen.dto.QueryRealTimeDataDTO;
import com.chen.entity.RealTimeData;
import com.chen.service.RealTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class RealTimeServiceImpl implements RealTimeService {

    private static final String REAL_TIME_DATE_POSTFIX = "_real_time_data";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public List<RealTimeData> queryRealTimeData(QueryRealTimeDataDTO queryRealTimeDataDTO) {
        String key = queryRealTimeDataDTO.getSource() + REAL_TIME_DATE_POSTFIX;
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        Set<String> realTimeDataSet = zSetOps.reverseRangeByScore(key, 0, System.currentTimeMillis(), 0, queryRealTimeDataDTO.getSize());
        List<RealTimeData> realTimeDataList = new ArrayList<>();
        if (Objects.nonNull(realTimeDataSet) && !realTimeDataSet.isEmpty()) {
            for (String realTimeDataJsonString : realTimeDataSet) {
                RealTimeData realTimeData = JSON.parseObject(realTimeDataJsonString, RealTimeData.class);
                realTimeDataList.add(realTimeData);
            }
        }
        return realTimeDataList;
    }
}
