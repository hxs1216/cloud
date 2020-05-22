package com.biyao.security.dto;

import java.util.List;

/**
 * @ClassName: RoleDataAuthBindDTO
 * @Description: 角色绑定数据权限DTO
 * @author tyjuncai
 * @date 2018/12/24 11:38
 */
public class RoleDataAuthBindDTO {

	/**
	 * 角色id
	 */
	private Long roleId;

	/**
	 * 角色类型
	 */
	private Integer roleType;

	/**
	 * 角色是否受权限控制
	 */
	private Integer ctrlType;

	/**
	 * 角色数据权限列表
	 */
	private List<RoleDataAuthDTO> roleDataAuthList;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

	public Integer getCtrlType() {
		return ctrlType;
	}

	public void setCtrlType(Integer ctrlType) {
		this.ctrlType = ctrlType;
	}

	public List<RoleDataAuthDTO> getRoleDataAuthList() {
        return roleDataAuthList;
    }

    public void setRoleDataAuthList(List<RoleDataAuthDTO> roleDataAuthList) {
        this.roleDataAuthList = roleDataAuthList;
    }

	@Override
	public String toString() {
		return "RoleDataAuthBindDTO{" +
				"roleId=" + roleId +
				", roleType=" + roleType +
				", ctrlType=" + ctrlType +
				", roleDataAuthList=" + roleDataAuthList +
				'}';
	}
}
