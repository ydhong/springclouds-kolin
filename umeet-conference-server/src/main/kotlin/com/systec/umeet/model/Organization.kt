package com.umeet.conference.model

import com.umeet.conference.base.BaseEntity
import javax.persistence.*

@Table(name="tb_organization")
class Organization(
        @Id
        @Column(name = "Id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var  id: String? = null,
        /**
         * 名称
         */
        @Column(name = "`name`")
        var name :String? = null,

        /**
         * 类型 0 用户创建 1 AD 组织 2 AD 安全组
         */
        @Column(name = "`type`")
        var type :String? = null,

        /**
         * 电话
         */
        @Column(name = "`phone`")
        var phone :String? = null,
        /**
         * 邮政编码
         */
        @Column(name = "`zip_code`")
        var zipCode :String? = null,
        /**
         * 地址
         */
        @Column(name = "`address`")
        var address :String? = null,
        /**
         * 描述
         */
        @Column(name = "`description`")
        var description :String? = null,
        /**
         * 父ID
         */
        @Column(name = "`pid`")
        var pid :String? = null,
        /**
         * AD域 节点所属的级别
         */
        @Column(name = "`levels`")
        var levels :String? = null,
        /**
         * AD域字段的更新时间
         */
        @Column(name = "`when_changed`")
        var whenChanged :String? = null,
        /**
         * AD域机构的唯一标识
         */
        @Column(name = "`usn_created`")
        var uSNCreated :String? = null,
        /**
         * 0: 禁用  ，  1：启用
         */
        @Column(name = "`is_enable`")
        var isEnable :String? = null,
        /**
         * 0:不可删除 其他待定
         */
        @Column(name = "`is_delete`")
        var isDelete :String? = null,
        /**
         * 排序字段
         */
        @Column(name = "`sort`")
        var sort :Int? = null,

        @Transient
        var  parentName :String? = null

) :BaseEntity(){}
