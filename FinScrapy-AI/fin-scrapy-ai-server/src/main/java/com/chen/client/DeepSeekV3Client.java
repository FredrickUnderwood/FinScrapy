package com.chen.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chen.constant.ClientConstant;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DeepSeekV3Client {

    private static final String LOG_PREFIX = "[DeepSeekV3Client]";

    private static final String BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

    private static final String MODEL = "deepseek-v3";

    @Value("${aliyun.api_key}")
    private String aliyunApiKey;

    public String doPost(String prompt) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(ClientConstant.DEEP_SEEK_V3_CLIENT_READ_TIMEOUT, TimeUnit.MINUTES)
                .connectTimeout(ClientConstant.DEEP_SEEK_V3_CLIENT_CONNECT_TIMEOUT, TimeUnit.MINUTES)
                .writeTimeout(ClientConstant.DEEP_SEEK_V3_CLIENT_WRITE_TIMEOUT, TimeUnit.MINUTES)
                .build();

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);

        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);

        JSONArray messages = new JSONArray();
        messages.add(userMessage);

        requestBody.put("messages", messages);

        Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("Authorization", "Bearer " + aliyunApiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toJSONString(), MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("{}Call api failed, response code: {}", LOG_PREFIX, response.code());
                return null;
            }
            return response.body().string();
        } catch (IOException e) {
            log.error("{}Call api failed, error: {}", LOG_PREFIX, e.getMessage(), e);
        }
        return null;
    }


}
