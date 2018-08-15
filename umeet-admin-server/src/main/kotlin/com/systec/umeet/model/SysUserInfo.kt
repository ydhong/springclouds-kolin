package com.systec.umeet.model

import javax.persistence.Column

class SysUserInfo(

        /**
         * 角色名称
         */
        @Column(name = "`group_id`")
        override var groupId :Int? = null,
        /**
         * 扩展属性 子级列表
         */
        @Column(name = "`group_name`")
        var groupName: String ?= null

) :SysUser(){}
