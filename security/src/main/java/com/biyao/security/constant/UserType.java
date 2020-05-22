package com.biyao.security.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: UserType
 * @Description: 账号类型
 * @author tyjuncai
 * @date 2019/1/4 11:11
 */
public enum UserType {
    platform(0, "平台账号"),
    business(1, "业务方账号");
    private int value;
    private String name;

    private static Map<Integer, UserType> valueMap = new HashMap<>(UserType.values().length);
    private static Map<String, UserType> nameMap = new HashMap<>(UserType.values().length);

    static {
        valueMap = new HashMap<>();
        for (UserType type : UserType.values()) {
            valueMap.put(type.getValue(), type);
            nameMap.put(type.getName(), type);
        }
    }

    UserType(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static UserType fromValue(int value) {
        return valueMap.get(value);
    }

    public static UserType fromName(String name) {
        return nameMap.get(name);
    }
}
