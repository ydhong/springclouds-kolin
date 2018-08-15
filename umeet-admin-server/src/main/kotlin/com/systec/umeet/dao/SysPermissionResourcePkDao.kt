package com.systec.umeet.dao
import com.systec.umeet.model.SysPermissionResourcePk
import com.systec.umeet.utills.MyMapper

interface SysPermissionResourcePkDao :MyMapper<SysPermissionResourcePk>{
    abstract fun insertPermissionResources(list: List<SysPermissionResourcePk>): Int?

    abstract fun deletePermissionResources(map: Map<String, Any>): Int?
}
