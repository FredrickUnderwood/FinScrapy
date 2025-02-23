package com.chen.service.impl;

import com.chen.constant.FinScrapyConstant;
import com.chen.context.UserContext;
import com.chen.entity.Subscription;
import com.chen.entity.User;
import com.chen.enums.LevelSubscriptionEnum;
import com.chen.enums.RespStatusEnum;
import com.chen.mapper.SubscriptionMapper;
import com.chen.mapper.UserMapper;
import com.chen.redis.SubscriptionRedisMapper;
import com.chen.service.SubscriptionService;
import com.chen.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SubscriptionRedisMapper subscriptionRedisMapper;

    @Override
    @Transactional
    public ResultVO<Void> createSubscription(Subscription subscription) {
        subscription.setUserId(UserContext.getUserId());
        User user = userMapper.findById(subscription.getUserId());
        if (Objects.isNull(user) || user.getStatus() == 0) {
            return ResultVO.error(RespStatusEnum.SUBSCRIPTION_USER_ILLEGAL);
        }

        LevelSubscriptionEnum level = LevelSubscriptionEnum.getLevelSubscriptionEnumByLevel(user.getLevel());
        Integer activeSubscription = subscriptionMapper.countActiveByUserId(subscription.getUserId());
        if (Objects.isNull(level) || activeSubscription.equals(level.getLevel())) {
            return ResultVO.error(RespStatusEnum.SUBSCRIPTION_TOO_MANY);
        }

        if ((!level.getEmailNotify() && subscription.getEmailNotify() == 1) || (!level.getPushNotify() && subscription.getEmailNotify() == 1)) {
            return ResultVO.error(RespStatusEnum.SUBSCRIPTION_NOTIFY_ILLEGAL);
        }

        subscription.setStatus(1);
        subscription.setCreatedTime(LocalDateTime.now());
        subscriptionMapper.insert(subscription);

        /*
        将Keyword插入Redis对应的队列中
         */
        if (subscription.getPushNotify() == 1) {
            subscriptionRedisMapper.addSubscriptionPushToRedis(subscription.getKeywords(), user.getId());
        }
        if (subscription.getEmailNotify() == 1) {
            subscriptionRedisMapper.addSubscriptionEmailToRedis(subscription.getKeywords(), user.getEmail());
        }
        subscriptionRedisMapper.addSubscriptionKeywordsToRedis(subscription.getKeywords());
        return ResultVO.success();
    }

    @Override
    @Transactional
    public ResultVO<Void> updateSubscription(Subscription subscription) {
        subscription.setUserId(UserContext.getUserId());
        Subscription oldSubscription = subscriptionMapper.findById(subscription.getId());
        User user = userMapper.findById(oldSubscription.getUserId());
        LevelSubscriptionEnum level = LevelSubscriptionEnum.getLevelSubscriptionEnumByLevel(user.getLevel());
        assert level != null;
        if (oldSubscription.getStatus() == 0 && subscription.getStatus() == 0) {
            // 始终禁用，无需改变
            return ResultVO.success();
        } else if (oldSubscription.getStatus() == 1 && subscription.getStatus() == 0) {
            // 禁用该Subscription
            subscription.setEmailNotify(0);
            subscription.setPushNotify(0);
            if (oldSubscription.getPushNotify() == 1) {
                subscriptionRedisMapper.removeSubscriptionPushFromRedis(oldSubscription.getKeywords(), oldSubscription.getUserId());
            }
            if (oldSubscription.getEmailNotify() == 1) {
                subscriptionRedisMapper.removeSubscriptionEmailFromRedis(oldSubscription.getKeywords(), user.getEmail());
            }
            subscriptionRedisMapper.removeSubscriptionKeywordsFromRedis(oldSubscription.getKeywords());
        } else if (oldSubscription.getStatus() == 1 && subscription.getStatus() == 1) {
            // 更新Subscription参数
            ResultVO<Void> result = updateSubscriptionHelper(oldSubscription, subscription, level, user);
            if (result != null) return result;
        } else {
            // 开启Subscription
            // 检查是否开启过多Subscription
            Integer activeSubscription = subscriptionMapper.countActiveByUserId(oldSubscription.getUserId());
            if (activeSubscription.equals(level.getMaximumSubscription())) {
                return ResultVO.error(RespStatusEnum.SUBSCRIPTION_TOO_MANY);
            }
            ResultVO<Void> result = updateSubscriptionHelper(oldSubscription, subscription, level, user);
            if (result != null) return result;
        }
        subscriptionMapper.update(subscription);
        return ResultVO.success();
    }

    private ResultVO<Void> updateSubscriptionHelper(Subscription oldSubscription, Subscription subscription, LevelSubscriptionEnum level, User user) {
        // 开启PushNotify
        if (oldSubscription.getPushNotify() == 0 && subscription.getPushNotify() == 1) {
            if (!level.getPushNotify()) {
                return ResultVO.error(RespStatusEnum.SUBSCRIPTION_NOTIFY_ILLEGAL);
            }
        }
        // 开启EmailNotify
        if (oldSubscription.getEmailNotify() == 0 && subscription.getEmailNotify() == 1) {
            if (!level.getEmailNotify()) {
                return ResultVO.error(RespStatusEnum.SUBSCRIPTION_NOTIFY_ILLEGAL);
            }

        }
        if (oldSubscription.getPushNotify() == 1) {
            subscriptionRedisMapper.removeSubscriptionPushFromRedis(oldSubscription.getKeywords(), user.getId());
        }
        if (oldSubscription.getEmailNotify() == 1) {
            subscriptionRedisMapper.removeSubscriptionEmailFromRedis(oldSubscription.getKeywords(), user.getEmail());
        }
        if (subscription.getPushNotify() == 1) {
            subscriptionRedisMapper.addSubscriptionPushToRedis(subscription.getKeywords(), user.getId());
        }
        if (subscription.getEmailNotify() == 1) {
            subscriptionRedisMapper.addSubscriptionEmailToRedis(subscription.getKeywords(), user.getEmail());
        }
        // 更新keywords集合
        if (!oldSubscription.getKeywords().equals(subscription.getKeywords())) {
            subscriptionRedisMapper.removeSubscriptionKeywordsFromRedis(oldSubscription.getKeywords());
            subscriptionRedisMapper.addSubscriptionKeywordsToRedis(subscription.getKeywords());
        }
        return null;
    }
}
