package com.umeet.conference.model

import com.umeet.conference.base.BaseEntity
import javax.persistence.*

@Table(name = "`tb_user`")
class User(
        @Id
        @Column(name = "Id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var  id: String? = null,
        /**
         * 登录名
         */
        @Column(name = "`account`")
        var account :String? = null,

        /**
         * 类型 0 用户创建 1 AD 组织 2 AD 安全组
         */
        @Column(name = "`type`")
        var type :String? = null,

        /**
         * Umeeting账号ID
         */
        @Column(name = "`host_id`")
        var hostId :String? = null,
        /**
         * 密码
         */
        @Column(name = "`password`")
        var password :String? = null,
        /**
         * 部门ID
         */
        @Column(name = "`org_id`")
        var orgId :String? = null,
        /**
         * 角色ID
         */
        @Column(name = "`role_id`")
        var roleId :String? = null,
        /**
         * 名字
         */
        @Column(name = "`name`")
        var name :String? = null,
        /**
         * 电话
         */
        @Column(name = "`phone`")
        var phone :String? = null,
        /**
         * 邮箱
         */
        @Column(name = "`email`")
        var email :String? = null,
        /**
         * 职位
         */
        @Column(name = "`position`")
        var position :String? = null,
        /**
         * 是否启用  0：禁用， 1：启用
         */
        @Column(name = "`is_enable`")
        var isEnable :Int? = null,
        /**
         * 性别
         */
        @Column(name = "`gender`")
        var gender :Int? = null,
        /**
         * 排序
         */
        @Column(name = "`description`")
        var description :String? = null,
        /**
         * 排序
         */
        @Column(name = "`sort`")
        var sort :Int? = null,
        /**
         * 卡号
         */
        @Column(name = "`cardno`")
        var cardNo :String? = null

): BaseEntity(){

}
