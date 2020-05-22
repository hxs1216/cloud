package com.biyao.security.constant;

/**
 * @ClassName: DataAuthFdepType
 * @Description: 金融业务系统相关数据权限数据项类型
 * @author tyjuncai
 * @date 2018/12/24 18:33
 */
public enum DataAuthFdepType {

    /**
     * 仅查看自身数据
     */
    OWN("own", "仅查看自身数据"),

    /**
     * 查看自身及自身发展下级
     */
    OWN_AND_SUBORDINATE("own-and-subordinate","查看自身及自身发展下级"),

    /**
     * 查看自身及所有下级
     */
    OWN_AND_ALL_SUBORDINATE("own-and-all-subordinate", "查看自身及所有下级"),

    /**
     * 查看全部
     */
    NO_LIMIT("no-limit", "查看全部"),
    ;

    private static final EnumMap<String, DataAuthFdepType> enumMap;

    static {
        enumMap = new EnumMap<>(DataAuthFdepType.class, DataAuthFdepType::getValue);
    }

    public static DataAuthFdepType fromValue(String value) {
        return enumMap.fromValue(value);
    }


    private String value;
    private String name;

    DataAuthFdepType(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
