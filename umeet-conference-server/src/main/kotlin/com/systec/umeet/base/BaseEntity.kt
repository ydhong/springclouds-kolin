package com.umeet.conference.base;

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
		@Transient var rows: Int  = 10
): Serializable {


}
