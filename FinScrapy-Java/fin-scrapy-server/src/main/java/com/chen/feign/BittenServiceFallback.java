package com.chen.feign;

import com.chen.dto.SendRequest;
import org.springframework.stereotype.Component;

@Component
public class BittenServiceFallback implements BittenServiceFeignClient {
    @Override
    public String send(SendRequest sendRequest) {
        return "Send message failed!";
    }
}
