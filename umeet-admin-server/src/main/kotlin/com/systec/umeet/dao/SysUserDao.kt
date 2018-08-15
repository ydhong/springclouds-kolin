package com.systec.umeet.dao
import com.systec.umeet.model.SysUser
import com.systec.umeet.model.SysUserInfo
import com.systec.umeet.utills.MyMapper

interface SysUserDao :MyMapper<SysUser>{
    abstract fun selectUserGroup(userId: Int?): SysUserInfo

    abstract fun selectGroupNoUsers(map: Map<String, Any>): List<SysUser>

    abstract fun selectGroupUsers(map: Map<String, Any>): List<SysUser>
}
