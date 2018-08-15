package com.systec.umeet.model

import com.systec.umeet.base.BaseEntity
import javax.persistence.*

@Table(name = "sys_menu")
open class SysMenu(
        @Id
        @Column(name = "Id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var  id: Int? = null,
        /**
         * 名称
         */
        @Column(name = "`title`")
        var title :String? = null,
        /**
         *图表
         */
        @Column(name = "`icon`")
        var icon :String? = null,
        /**
         *父节点
         */
        @Column(name = "`parent_id`")
        var parentId :Int? = null,

        /** 排序  */
        @Column(name = "`sort`")
        var sort: Int? = null,
        /**
         *路径
         */
        /**
         * 名称
         */
        @Column(name = "`name`")
        override var name :String? = null,

        @Column(name = "`path`")
        var path :String? = null,
        /**
         *路径
         */
        @Column(name = "`level`")
        var level :Int? = null,
        /**
        *前端组件
        */
        @Column(name = "`component`")
        var component :String? = null,
        /**
         *描述
         */
        @Column(name = "`description`")
        var description :String? = null,
        /**
         *
         */
        @Column(name = "`state`")
        var state :Int? = null

) :BaseEntity(){}
