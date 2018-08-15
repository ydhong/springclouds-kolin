package com.systec.umeet.model

import com.systec.umeet.base.BaseEntity
import javax.persistence.*

@Table(name = "sys_permission_resource_pk")
class SysPermissionResourcePk(
        /**
         * 用户ID
         */
        @Column(name = "`resource_id`")
        var resourceId :Long? = null,
        /**
         * 用户组ID
         */
        @Column(name = "`permission_d`")
        var permissionId :Int? = null
):BaseEntity(){}
