package com.systec.umeet.dao
import com.systec.umeet.model.SysPermission
import com.systec.umeet.utills.MyMapper
interface SysPermissionDao :MyMapper<SysPermission>{
    abstract fun selectRoleNoPermissions(map: Map<String, Any>): List<SysPermission>

    abstract fun selectRolePermissions(map: Map<String, Any>): List<SysPermission>
}
