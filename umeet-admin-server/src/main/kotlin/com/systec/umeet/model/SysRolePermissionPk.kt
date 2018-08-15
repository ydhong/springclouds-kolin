package com.systec.umeet.model

import com.systec.umeet.base.BaseEntity
import javax.persistence.*

@Table(name = "tb_role")
class SysRolePermissionPk(
        /**
         * 角色ID
         */
        @Column(name = "`roleId`")
        var roleId :Int? = null,
        /**
         * 权限ID
         */
        @Column(name = "`permission_id`")
        var permissionId :Long? = null
):BaseEntity(){}
