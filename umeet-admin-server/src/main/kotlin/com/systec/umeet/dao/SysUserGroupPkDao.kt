package com.systec.umeet.dao
import com.systec.umeet.model.SysUserGroupPk
import com.systec.umeet.utills.MyMapper

interface SysUserGroupPkDao :MyMapper<SysUserGroupPk>{
    abstract fun insertGroupUsers(list: List<SysUserGroupPk>): Int?

    abstract fun deleteGroupUsers(map: Map<String, Any>): Int?

    abstract fun updateGroupUser(map: Map<String, Any>): Int?
}
