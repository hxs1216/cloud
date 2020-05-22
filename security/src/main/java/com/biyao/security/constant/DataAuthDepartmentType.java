package com.biyao.security.constant;

/**
 * @ClassName: DataAuthDepartmentType
 * @Description: 部门相关数据权限数据项类型
 * @author tyjuncai
 * @date 2018/12/24 18:33
 */
public enum DataAuthDepartmentType {
	/**
	 * 仅可见自己所在部门
	 */
	OWN("own", "仅可见自己所在部门"),

	/**
	 * 依赖于自定义关联的部门
	 */
	DEFINE("define", "依赖于自定义关联的部门"),
	;

	private static final EnumMap<String, DataAuthDepartmentType> enumMap;

	static {
		enumMap = new EnumMap<>(DataAuthDepartmentType.class, DataAuthDepartmentType::getValue);
	}

	public static DataAuthDepartmentType fromValue(String value) {
		return enumMap.fromValue(value);
	}


	private String value;
	private String name;

	DataAuthDepartmentType(String value, String name) {
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
