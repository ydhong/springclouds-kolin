package com.systec.umeet.base;

import java.io.Serializable
import javax.persistence.*
/**
 * 实体基类
 * @author leo.aqing
 *
 */
@Entity
open class BaseEntity(
		@Transient var  page: Int = 1,
		@Transient var size: Int  = 10,
		@Transient open var name: String ?= null
): Serializable {


}
