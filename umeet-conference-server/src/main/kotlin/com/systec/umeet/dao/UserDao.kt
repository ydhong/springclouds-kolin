package com.umeet.conference.dao

import com.umeet.conference.model.User
import com.umeet.conference.util.MyMapper

interface UserDao :MyMapper<User>{
    abstract fun findByOrgIds(orgIds: MutableList<String>, enable: Int?): List<User>
    abstract fun enableUserByType(type: String)
    abstract fun disableUserByType(type: String)
    abstract fun queryUserMaxSort():User

}
