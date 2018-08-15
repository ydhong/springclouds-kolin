package com.systec.umeet.contants;

import com.systec.umeet.utills.ListUtils;

import java.util.List;

/**
 * @description:
 * @author: super.wu
 * @date: Created in 2018/6/11 0011
 * @modified By:
 * @version: 1.0
 **/
public class UserEntity {

    public int userId;

    public String username;

    public List<String> roles;

    public UserEntity(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public UserEntity(int userId, String username, List<String> roles) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
    }

    /**
     * 判断用户是否拥有角色
     * @return
     */
    public boolean isRoles() {
        return !ListUtils.isEmpty(this.roles);
    }

    /**
     * 判断是否是超级管理员
     * @return
     */
    public boolean isAdmin() {
        if (this.isRoles() && this.roles.contains(SysConstant.SUPER_ADMIN_ROLE_CODE)) {
            return true;
        }
        return false;
    }

}
