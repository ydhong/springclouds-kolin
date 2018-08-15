package com.systec.umeet.model

import com.systec.umeet.base.BaseEntity
import javax.persistence.*
import kotlin.jvm.Transient

@Table(name = "sys_permission_menu_pk")
class SysPermissionMenuPk(
        /**
         * 菜单ID
         */
        @Column(name = "`menu_id`")
        var menuId :Int? = null,
        /**
         * 权限ID
         */
        @Column(name = "`permission_id`")
        var permissionId :Int? = null


) : BaseEntity() {


}
