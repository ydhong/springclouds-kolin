package com.systec.umeet.model

import com.systec.umeet.base.BaseEntity
import javax.persistence.*

@Table(name = "sys_user_group_pk")
class SysUserGroupPk(
        /**
         * 用户ID
         */
        @Column(name = "`user_id`")
        var userId :Int? = null,
        /**
         * 用户组ID
         */
        @Column(name = "`group_id`")
        var groupId :Int? = null
):BaseEntity(){}
