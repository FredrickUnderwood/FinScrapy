package com.chen.redis;

import com.alibaba.fastjson.JSON;
import com.chen.constant.FinScrapyConstant;
import com.chen.entity.RealTimeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class RealTimeDataRedisMapper {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public List<RealTimeData> getRealTimeTitlesSpecifiedSource(String source, Long maxScore, Long minScore) {
        String key = source + FinScrapyConstant.REAL_TIME_DATA_REDIS_KEY_POSTFIX;
        Set<String> realTimeDataJsonString = redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);
        List<RealTimeData> results = new ArrayList<>();
        if (Objects.nonNull(realTimeDataJsonString) && !realTimeDataJsonString.isEmpty()) {
            realTimeDataJsonString.forEach(item -> {
                results.add(JSON.parseObject(item, RealTimeData.class));
            });
        }
        return results;
    }

}
