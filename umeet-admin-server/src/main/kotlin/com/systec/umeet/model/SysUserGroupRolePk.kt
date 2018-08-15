package com.systec.umeet.model

import com.systec.umeet.base.BaseEntity
import javax.persistence.*

@Table(name = "sys_user_group_role_pk")
class SysUserGroupRolePk(
        /**
         * 用户ID
         */
        @Column(name = "`role_id`")
        var roleId :Int? = null,
        /**
         * 用户组ID
         */
        @Column(name = "`group_id`")
        var groupId :Int? = null
):BaseEntity(){}
