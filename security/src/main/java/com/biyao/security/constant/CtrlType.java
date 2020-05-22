package com.biyao.security.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 角色数据权限受控类型
 *
 * @Author tanglh
 * @Date 2019/1/9 10:35
 */
public enum CtrlType {

    CTRL_ENABLE(0, "受控制"),
    CTRL_DISABLE(1, "不受控制"),;

    private int value;
    private String name;

    private static Map<Integer, CtrlType> valueMap = new HashMap<>(CtrlType.values().length);

    static {
        valueMap = new HashMap<>();
        for (CtrlType type : CtrlType.values()) {
            valueMap.put(type.getValue(), type);
        }
    }

    CtrlType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static CtrlType fromValue(Integer value) {
        return valueMap.get(value);
    }


}
