package com.chen.redis;


import com.chen.constant.FinScrapyConstant;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@Slf4j
public class SubscriptionRedisMapper {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String REMOVE_SUBSCRIPTION_EMAIL_FROM_REDIS_LUA_SCRIPT = "lua/remove_subscription_email_from_redis.lua";

    private static final String REMOVE_SUBSCRIPTION_PUSH_FROM_REDIS_LUA_SCRIPT = "lua/remove_subscription_push_from_redis.lua";

    private static final String REMOVE_SUBSCRIPTION_KEYWORDS_FROM_REDIS_LUA_SCRIPT = "lua/remove_subscription_keywords_from_redis.lua";

    private DefaultRedisScript<Long> removeSubscriptionEmailFromRedisScript;

    private DefaultRedisScript<Long> removeSubscriptionPushFromRedisScript;

    private DefaultRedisScript<Long> removeSubscriptionKeywordsFromRedisScript;

    @PostConstruct
    public void initLuaScript() {
        removeSubscriptionEmailFromRedisScript = new DefaultRedisScript<>();
        removeSubscriptionEmailFromRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(REMOVE_SUBSCRIPTION_EMAIL_FROM_REDIS_LUA_SCRIPT)));
        removeSubscriptionEmailFromRedisScript.setResultType(Long.class);

        removeSubscriptionPushFromRedisScript = new DefaultRedisScript<>();
        removeSubscriptionPushFromRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(REMOVE_SUBSCRIPTION_PUSH_FROM_REDIS_LUA_SCRIPT)));
        removeSubscriptionPushFromRedisScript.setResultType(Long.class);

        removeSubscriptionKeywordsFromRedisScript = new DefaultRedisScript<>();
        removeSubscriptionKeywordsFromRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(REMOVE_SUBSCRIPTION_KEYWORDS_FROM_REDIS_LUA_SCRIPT)));
        removeSubscriptionKeywordsFromRedisScript.setResultType(Long.class);
    }

    public void removeSubscriptionEmailFromRedis(String keywords, String email) {
        for (String keyword: keywords.split(",")) {
            String key = FinScrapyConstant.SUBSCRIPTION_KEYWORD_REDIS_PREFIX + keyword + FinScrapyConstant.SUBSCRIPTION_KEYWORD_REDIS_EMAIL_POSTFIX;
            executeLuaScript(removeSubscriptionEmailFromRedisScript, List.of(key), email);
        }
    }

    public void removeSubscriptionPushFromRedis(String keywords, Long userId) {
        for (String keyword: keywords.split(",")) {
            String key = FinScrapyConstant.SUBSCRIPTION_KEYWORD_REDIS_PREFIX + keyword + FinScrapyConstant.SUBSCRIPTION_KEYWORD_REDIS_PUSH_POSTFIX;
            executeLuaScript(removeSubscriptionPushFromRedisScript, List.of(key), String.valueOf(userId));
        }
    }

    public void removeSubscriptionKeywordsFromRedis(String keywords) {
        for (String keyword: keywords.split(",")) {
            String key = FinScrapyConstant.SUBSCRIPTION_KEYWORDS_REDIS_KEY;
            executeLuaScript(removeSubscriptionKeywordsFromRedisScript, List.of(key), keyword);
        }
    }

    public void addSubscriptionEmailToRedis(String keywords, String email) {
        for (String keyword: keywords.split(",")) {
            String key = FinScrapyConstant.SUBSCRIPTION_KEYWORD_REDIS_PREFIX + keyword + FinScrapyConstant.SUBSCRIPTION_KEYWORD_REDIS_EMAIL_POSTFIX;
            redisTemplate.opsForHash().increment(key, email, 1);
        }
    }

    public void addSubscriptionPushToRedis(String keywords, Long userId) {
        for (String keyword: keywords.split(",")) {
            String key = FinScrapyConstant.SUBSCRIPTION_KEYWORD_REDIS_PREFIX + keyword + FinScrapyConstant.SUBSCRIPTION_KEYWORD_REDIS_PUSH_POSTFIX;
            redisTemplate.opsForHash().increment(key, String.valueOf(userId), 1);
        }
    }

    public void addSubscriptionKeywordsToRedis(String keywords) {
        for (String keyword: keywords.split(",")) {
            redisTemplate.opsForHash().increment(FinScrapyConstant.SUBSCRIPTION_KEYWORDS_REDIS_KEY, keyword, 1);
        }
    }

    public Set<String> getActiveKeywords() {
        String keywordKey = FinScrapyConstant.SUBSCRIPTION_KEYWORDS_REDIS_KEY;
        Set<String> result = new HashSet<>();
        redisTemplate.opsForHash().keys(keywordKey).forEach(item -> result.add((String) item));
        return result;
    }

    public Set<String> getEmailsByKeyword(String keyword) {
        String key = FinScrapyConstant.SUBSCRIPTION_KEYWORD_REDIS_PREFIX + keyword + FinScrapyConstant.SUBSCRIPTION_KEYWORD_REDIS_EMAIL_POSTFIX;
        Set<String> result = new HashSet<>();
        redisTemplate.opsForHash().keys(key).forEach(item -> result.add((String) item));
        return result;
    }

    public Set<String> getPushByKeyword(String keyword) {
        String key = FinScrapyConstant.SUBSCRIPTION_KEYWORD_REDIS_PREFIX + keyword + FinScrapyConstant.SUBSCRIPTION_KEYWORD_REDIS_PUSH_POSTFIX;
        Set<String> result = new HashSet<>();
        redisTemplate.opsForHash().keys(key).forEach(item -> result.add((String) item));
        return result;
    }


    private void executeLuaScript(RedisScript<Long> luaScript, List<String> keys, String... argv) {
        String[] argsArray = argv != null ? argv : new String[0];
        try {
            redisTemplate.execute(luaScript, keys, (Object[]) argsArray);
        } catch (Exception e) {
            log.error("Execute lua script failed! e: {}", e.getMessage(), e);
        }
    }
}
