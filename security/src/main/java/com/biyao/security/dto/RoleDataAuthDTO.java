package com.biyao.security.dto;

import java.util.List;

/**
 * @ClassName: RoleDataAuthDTO
 * @Description: 数据权限DTO
 * @author tyjuncai
 * @date 2018/12/25 17:15
 */
public class RoleDataAuthDTO {

	/**
	 * 数据权限类型ID
	 */
	private Long dataAuthTypeId;

	/**
	 * 数据项列表
	 */
	private List<String> dataList;

	/**
	 * 数据项类型
	 */
	private String dataType;

	public Long getDataAuthTypeId() {
		return dataAuthTypeId;
	}

	public void setDataAuthTypeId(Long dataAuthTypeId) {
		this.dataAuthTypeId = dataAuthTypeId;
	}

	public List<String> getDataList() {
		return dataList;
	}

	public void setDataList(List<String> dataList) {
		this.dataList = dataList;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

}
