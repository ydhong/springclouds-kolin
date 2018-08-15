package com.systec.umeet.dao
import com.systec.umeet.model.SysUserGroupRolePk
import com.systec.umeet.utills.MyMapper

interface SysUserGroupRolePkDao :MyMapper<SysUserGroupRolePk>{
    abstract fun insertGroupRoles(list: List<SysUserGroupRolePk>): Int?

    abstract fun deleteGroupRoles(map: Map<String, Any>): Int?
}
