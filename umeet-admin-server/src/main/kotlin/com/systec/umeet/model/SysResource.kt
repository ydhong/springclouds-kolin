package com.systec.umeet.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.systec.umeet.base.BaseEntity
import javax.persistence.*

@Table(name = "`sys_resource`")
class SysResource(
        @Id
        @Column(name = "Id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var  id: Long? = null,
        /**
         * 名称
         */
        @Column(name = "`name`")
        override var name :String? = null,

        @Column(name = "`code`")
        var code :String ?= null,

        /**
         *
         */
        @Column(name = "`page_elements`")
        var pageElements :String? = null,

        /**
         * 路径
         */
        @Column(name = "`url`")
        var url :String? = null,
        /**
         * 路径请求类型
         */
        @Column(name = "`url_request_type`")
        var urlRequestType :String? = null,
        /**
         * 描述
         */
        @Column(name = "`description`")
        var description :String? = null,
        /**
         * 父节点
         */
        @JsonSerialize(using = ToStringSerializer::class)
        @Column(name = "`parent_id`")
        var parentId :Long? = null,
        /**
         * 父节点名字
         */
        @Column(name = "`parent_name`")
        var parentName :String? = null,
        /**
         * 节点深度
         */
        @Column(name = "`level`")
        var level :Int? = null,
        /**
         *
         */
        @Column(name = "`state`")
        var state :Int? = null,

        /** 扩展属性 资源组下的资源信息  */
        @Transient
        var childrens: MutableList<SysResource>? = null,

        /** 扩展属性 资源是否被选中  */
        @Transient
        var checked: Boolean = false

): BaseEntity(){

}
