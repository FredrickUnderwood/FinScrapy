package com.chen.service;

import com.chen.entity.Subscription;
import com.chen.enums.RespStatusEnum;
import com.chen.vo.ResultVO;

public interface SubscriptionService {

    ResultVO<Void> createSubscription(Subscription subscription);

    ResultVO<Void> updateSubscription(Subscription subscription);
}
