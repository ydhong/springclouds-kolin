package com.systec.umeet.model

import com.systec.umeet.base.BaseEntity
import javax.persistence.*
@Table(name = "sys_role")
class SysRole(
        @Id
        @Column(name = "Id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var  id: Int? = null,
        /**
         * 角色名称
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
