package com.biyao.security.provider;


import com.biyao.security.dto.RoleDataAuthBindDTO;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述 用户权限对象
 *
 * @author hxs
 * @date 2020/5/22
 */
public class SecurityAuthority implements Serializable {

    private static final long serialVersionUID = 2576277480360711248L;

    /**
     * 用户对应的数据权限
     */
    List<RoleDataAuthBindDTO> dataAuthorityList;

    public List<RoleDataAuthBindDTO> getDataAuthorityList() {
        return dataAuthorityList;
    }

    public void setDataAuthorityList(List<RoleDataAuthBindDTO> dataAuthorityList) {
        this.dataAuthorityList = dataAuthorityList;
    }

    @Override
    public String toString() {
        return "SecurityAuthority{" +
                "dataAuthorityList=" + dataAuthorityList +
                '}';
    }
}
