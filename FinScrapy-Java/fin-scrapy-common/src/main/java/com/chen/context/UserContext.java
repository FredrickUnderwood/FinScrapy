package com.chen.context;

public class UserContext {
    public static ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        userThreadLocal.set(userId);
    }

    public static Long getUserId() {
        return userThreadLocal.get();
    }

    public static void removeUserId() {
        userThreadLocal.remove();
    }
}
