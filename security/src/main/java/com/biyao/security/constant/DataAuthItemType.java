package com.biyao.security.constant;

/**
 * @ClassName: DataAuthItemType
 * @Description: 数据权限项类型
 * @author tyjuncai
 * @date 2018/12/24 14:18
 */
public enum DataAuthItemType {
    /**
     * 设备等级
     */
    DEVICE_LEVEL(1,"设备等级"),

    /**
     * 设备型号
     */
    DEVICE_MODEL(2,"设备型号"),

    /**
     * 车辆分组
     */
    CAR_GROUP(3,"车辆分组"),

    /**
     * 资方
     */
    CAPITAL(4,"资方"),

    /**
     * 业务方
     */
    BUSINESS(5, "业务方"),

    /**
     * 部门
     */
    DEPARTMENT(6,"部门"),

    /**
     * 渠道
     */
    CHANNEL(7, "渠道"),

    /**
     * 渠道分组
     */
    CHANNEL_GROUP(8, "渠道分组"),
    ;

    private static final EnumMap<Integer, DataAuthItemType> enumMap;

    static {
        enumMap = new EnumMap<>(DataAuthItemType.class, DataAuthItemType::getValue);
    }

    public static DataAuthItemType fromValue(int value) {
        return enumMap.fromValue(value);
    }


    private int value;
    private String name;

    DataAuthItemType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
