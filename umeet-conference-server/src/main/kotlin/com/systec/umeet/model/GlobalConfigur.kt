package com.umeet.conference.model
import com.umeet.conference.base.BaseEntity
import javax.persistence.*

@Table(name = "tb_global_configur")
class GlobalConfigur(

        /**
         * 名称
         */
        @Id
        @Column(name = "`name`")
        var name :String? = null,
        /**
         * 值
         */
        @Column(name = "`value`")
        var value :String? = null


) : BaseEntity() {


}
