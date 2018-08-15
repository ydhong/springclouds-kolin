package com.systec.umeet.dao
import com.systec.umeet.model.SysUserRolePk
import com.systec.umeet.utills.MyMapper

interface SysUserRolePkDao :MyMapper<SysUserRolePk>{

    abstract fun insertUserRoles(list: List<SysUserRolePk>): Int?

    abstract fun deleteUserRoles(map: Map<String, Any>): Int?

}
