package com.chen.feign;

import com.chen.dto.SendRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bitten", fallback = BittenServiceFallback.class)
public interface BittenServiceFeignClient {

    @PostMapping("/bitten/send")
    String send(@RequestBody SendRequest sendRequest);
}
