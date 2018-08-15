package com.umeet.conference.model

import com.umeet.conference.base.BaseEntity
import javax.persistence.*

@Table(name = "tb_role")
class Role(
        @Id
        @Column(name = "Id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var  id: String? = null,
        /**
         * 角色名称
         */
        @Column(name = "`name`")
        var name :String? = null,
        /**
         * 描述
         */
        @Column(name = "`description`")
        var description :String? = null,
        /**
         * 0:系统角色 ,1:自定义角色
         * 备注:系统角色不可以删除
         */
        @Column(name = "`type`")
        var type :String? = null,
        /**
         * 角色创建时间
         */
        @Column(name = "`create_time`")
        var createTime :String? = null,
        /**
         * 是否启用 0：禁用， 1：启用
         */
        @Column(name = "`is_enable`")
        var isEnable :String? = null

) :BaseEntity(){}
