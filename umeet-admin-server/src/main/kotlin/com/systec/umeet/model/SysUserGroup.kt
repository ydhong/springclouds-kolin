package com.systec.umeet.model

import com.systec.umeet.base.BaseEntity
import javax.persistence.*

@Table(name = "sys_user_group")
class SysUserGroup(
        @Id
        @Column(name = "Id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var  id: Long? = null,
        /**
         * 名称
         */
        @Column(name = "`name`")
        override var name :String? = null,
        /**
         *
         */
        @Column(name = "`code`")
        var code :String? = null,
        /**
         *描述
         */
        @Column(name = "`description`")
        var description :String? = null

) :BaseEntity(){}
