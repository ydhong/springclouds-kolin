package com.systec.umeet.dao

import com.systec.umeet.model.SysRole
import com.systec.umeet.utills.MyMapper

interface SysRoleDao :MyMapper<SysRole>{
    /**
     * 加载不属于当前用户的角色列表
     * @param map 包含roleId和name模糊检索
     * @return
     */
    abstract fun selectUserNoRoles(map: Map<String, Any>): List<SysRole>


    abstract fun selectUserRoles(map: Map<String, Any>): List<SysRole>


    abstract fun selectUserGroupNoRoles(map: Map<String, Any>): List<SysRole>


    abstract fun selectUserGroupRoles(map: Map<String, Any>): List<SysRole>

    /**
     * 查询用户和用户所在组 关联的角色编码
     * @param userId
     * @return
     */
    abstract fun selectUserAndGroupResultRoles(userId: Long?): List<SysRole>
}
