package com.systec.umeet.dao

import com.systec.umeet.model.SysPermissionMenuPk
import com.systec.umeet.utills.MyMapper

interface SysPermissionMenuPkDao :MyMapper<SysPermissionMenuPk>{
    abstract fun insertPermissionMenus(list: List<SysPermissionMenuPk>): Int?
    abstract fun deletePermissionMenus(map: Map<String, Any>): Int?
}
