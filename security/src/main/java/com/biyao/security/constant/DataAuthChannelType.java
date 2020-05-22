package com.biyao.security.constant;

/**
 * @ClassName: DataAuthChannelType
 * @Description: 渠道相关数据权限数据项类型
 * @author tyjuncai
 * @date 2018/12/24 18:33
 */
public enum DataAuthChannelType {
    /**
     * 依赖于自身所在渠道
     */
    OWN_CHANNEL("own-channel", "依赖于自身所在渠道"),

    /**
     * 依赖于关联的渠道
     */
    RELATED_CHANNEL("related-channel","依赖于关联的渠道"),

    /**
     * 仅可见自己数据
     */
    OWN("own","仅可见自己数据"),

    /**
     * 依赖于自定义关联的渠道
     */
    ROLE_RELATED_CHANNEL("role-related-channel", "依赖于自定义关联的渠道"),

    /**
     * 依赖于自定义关联的渠道分组
     */
    ROLE_RELATED_CHANNEL_GROUP("role-related-channel-group", "依赖于自定义关联的渠道分组"),

    /**
     * 查看全部
     */
    NO_LIMIT("no-limit", "查看全部"),
    ;

    private static final EnumMap<String, DataAuthChannelType> enumMap;

    static {
        enumMap = new EnumMap<>(DataAuthChannelType.class, DataAuthChannelType::getValue);
    }

    public static DataAuthChannelType fromValue(String value) {
        return enumMap.fromValue(value);
    }


    private String value;
    private String name;

    DataAuthChannelType(String value, String name) {
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
