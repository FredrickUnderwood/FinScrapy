package com.chen.constant;

public class FinScrapyConstant {
    public static final String FULL_TEXT_ES_INDEX = "full_text_index";
    public static final Integer ES_REST_CLIENT_CONNECT_TIMEOUT = 5000;
    public static final Integer ES_REST_CLIENT_SOCKET_TIMEOUT = 60000;
    public static final Integer ES_REST_CLIENT_CONNECTION_REQUEST_TIMEOUT = 5000;
    public static final Long ES_REST_CLIENT_KEEP_ALIVE_TIME = 30000L;

    public static final String REAL_TIME_DATA_REDIS_KEY_POSTFIX = "_real_time_data";
    public static final String SUBSCRIPTION_KEYWORDS_REDIS_KEY = "subscription_keywords";
    public static final String SUBSCRIPTION_KEYWORD_REDIS_PREFIX = "subscription:";
    public static final String SUBSCRIPTION_KEYWORD_REDIS_EMAIL_POSTFIX = ":email";
    public static final String SUBSCRIPTION_KEYWORD_REDIS_PUSH_POSTFIX = ":push";

    public static final Integer MAXIMUM_FULL_TEXT_DATA_PAGE_SIZE = 50;

    public static final String USER_ID_JWT_CLAIM_KEY = "userId";
}
