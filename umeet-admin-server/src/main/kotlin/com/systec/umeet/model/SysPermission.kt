package com.systec.umeet.model
import com.systec.umeet.base.BaseEntity
import javax.persistence.*

@Table(name = "`sys_permission`")
open class SysPermission(
        @Id
        @Column(name = "Id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var  id: Int? = null,

        /**
         * 描述
         */
        @Column(name = "`description`")
        var description :String? = null,

        /**
         *
         */
        @Column(name = "`state`")
        var state :String? = null,

        /**
         * 名字
         */
        @Column(name = "`name`")
        override var name :String? = null


): BaseEntity(){

}
