package com.umeet.conference.model

import com.umeet.conference.base.BaseEntity
import javax.persistence.*

@Table(name="tb_privilege")
class Privilege (

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var  id: String? = null,

    /**
     * 权限代码
     */
    @Column(name = "`code`")
    var code :String? = null,
    /**
     * 权限名称
     */
    @Column(name = "`name`")
    var name :String? = null,
    /**
     * 权限排序
     */
    @Column(name = "`sort`")
    var sort :String? = null

    ) :BaseEntity(){}
