package com.umeet.conference.model

import com.umeet.conference.base.BaseEntity
import javax.persistence.*

@Table(name="tb_role_privilege")
class RoleAndPrivilege (
        @Id
        @Column(name = "Id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var  id: String? = null,
        /**
         * 角色ID
         */
        @Column(name = "`role_id`")
        var roleId :String? = null,
        /**
         * 权限ID
         */
        @Column(name = "`privilege_id`")
        var privilegeId :String? = null,
        /**
         * 是否拥有显示模块权限 0:是;1否   0有 1无
         */
        @Column(name = "`show`")
        var show :Int? = null,
        /**
         * 是否拥有读取权限 0:是;1否   0有 1无
         */
        @Column(name = "`read`")
        var read :Int? = null,
        /**
         * 是否拥有写入权限 0:是;1否   0有 1无
         */
        @Column(name = "`write`")
        var write :Int? = null,
        /**
         * 权限范围，0：全局，1：所在部门，2：所在部门及下属部门，3：本账号
         */
        @Column(name = "`scope`")
        var scope :Int? = null

        ) :BaseEntity() {
}
