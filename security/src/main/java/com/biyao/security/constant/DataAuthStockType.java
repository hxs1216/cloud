package com.biyao.security.constant;

/**
 * @ClassName: DataAuthStockType
 * @Description: 库存相关数据权限数据项类型
 * @author tyjuncai
 * @date 2019/1/25 11:32
 */
public enum  DataAuthStockType {
	/**
	 * 仅查看自有商品库存
	 */
	OWN("own","仅查看自有商品库存"),

	/**
	 * 查看商品所有共享库存和自有商品库存
	 */
	OWN_AND_SHARE("own-and-share", "查看商品所有共享库存和自有商品库存"),

	/**
	 * 查看全部库存
	 */
	NO_LIMIT("no-limit","查看全部"),
	;

	private static final EnumMap<String, DataAuthStockType> enumMap;

	static {
		enumMap = new EnumMap<>(DataAuthStockType.class, DataAuthStockType::getValue);
	}

	public static DataAuthStockType fromValue(String value) {
		return enumMap.fromValue(value);
	}


	private String value;
	private String name;

	DataAuthStockType(String value, String name) {
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
