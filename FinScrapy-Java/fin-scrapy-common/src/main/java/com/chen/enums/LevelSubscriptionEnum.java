package com.chen.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LevelSubscriptionEnum {

    ZERO(0, 0, false, false),
    ONE(1, 1, true, false),
    TWO(2, 2, true, false),
    THREE(3, 2, true, true),
    FOUR(4, 4, true, true),
    FIVE(5, 4, true, true),
    SIX(6, 10, true, true),
    SEVEN(7, 1000, true, true),;

    private final int level;

    private final int maximumSubscription;

    private final Boolean pushNotify;

    private final Boolean emailNotify;

    public static LevelSubscriptionEnum getLevelSubscriptionEnumByLevel(int level) {
        for (LevelSubscriptionEnum levelSubscriptionEnum : LevelSubscriptionEnum.values()) {
            if (levelSubscriptionEnum.getLevel() == level) {
                return levelSubscriptionEnum;
            }
        }
        return null;
    }
}
