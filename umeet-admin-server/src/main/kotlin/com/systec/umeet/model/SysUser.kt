package com.systec.umeet.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.systec.umeet.utills.ToStringDateSerializer
import com.systec.umeet.base.BaseEntity
import javax.persistence.*

@Table(name = "`sys_user`")
open class SysUser(
        @Id
        @Column(name = "Id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var  id: Int? = null,
        /**
         * 登录名
         */
        @Column(name = "`username`")
        var username :String? = null,

        /**
         * 密碼
         */
        @Column(name = "`password`")
        var password :String? = null,

        /**
         * 创建时间
         */
        @JsonSerialize(using = ToStringDateSerializer::class)
        @Column(name = "`create_time`")
        var createTime :Long? = null,
        /**
         * 密码最后修改时间
         */
        @JsonSerialize(using = ToStringDateSerializer::class)
        @Column(name = "`last_password_change`")
        var lastPasswordChange :Long? = null,
        /**
         *
         */
        @Column(name = "`state`")
        var state :String? = null,
        /**
         *
         */
        @Column(name = "`authorities`")
        var authorities :String? = null,
        /**
         * 名字
         */
        @Column(name = "`name`")
        override var name :String? = null,
        /** 增加属性 不含当前实体在数据库字段映射 **/

        /**
         * 用户资源
         */
        @Transient
        var resourceList: List<SysResource>? = null,

        @Transient
        open var groupId : Int ?= null

): BaseEntity(){

}
