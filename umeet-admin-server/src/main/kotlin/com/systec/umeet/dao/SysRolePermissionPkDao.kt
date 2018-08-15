package com.systec.umeet.dao
import com.systec.umeet.model.SysRolePermissionPk
import com.systec.umeet.utills.MyMapper

interface SysRolePermissionPkDao :MyMapper<SysRolePermissionPk>{
    abstract fun insertRolePermissions(list: List<SysRolePermissionPk>): Int?

    abstract fun deleteRolePermissions(map: Map<String, Any>): Int?
}
