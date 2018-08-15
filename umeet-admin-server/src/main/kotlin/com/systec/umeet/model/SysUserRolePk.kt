package com.systec.umeet.model

import com.systec.umeet.base.BaseEntity
import javax.persistence.*

@Table(name = "sys_user_role_pk")
class SysUserRolePk(
        /**
         * 用户ID
         */
        @Column(name = "`user_id`")
        var userId :Int? = null,
        /**
         * 用户组ID
         */
        @Column(name = "`role_id`")
        var roleId :Int? = null

):BaseEntity(){}
