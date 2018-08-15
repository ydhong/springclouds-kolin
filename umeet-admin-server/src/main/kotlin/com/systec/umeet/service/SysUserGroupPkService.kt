package com.systec.umeet.service
import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import com.systec.umeet.model.SysUser
import com.systec.umeet.base.BaseServiceImpl
import com.systec.umeet.contants.ResponsePage
import com.systec.umeet.dao.SysUserDao
import com.systec.umeet.dao.SysUserGroupPkDao
import com.systec.umeet.model.SysUserGroup
import com.systec.umeet.model.SysUserGroupPk
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
import java.util.*

@Service
class SysUserGroupPkService : BaseServiceImpl<SysUserGroupPk> (){

    @Autowired
    var sysUserGroupPkDao : SysUserGroupPkDao? = null

    @Autowired
    var sysUserDao : SysUserDao ?= null

    override fun getMapper(): Mapper<SysUserGroupPk> {
        return sysUserGroupPkDao!!
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: SysUserGroupPk): ResponsePage<SysUserGroupPk> {
        return super.selectPage(record.page , record.size ,record)
    }

    fun loadGroupNoRelationUsers(dto: SysUserGroupPk): ResponsePage<*> {
        PageHelper.startPage<Any>(dto.page, dto.size)
        val map = getQueryParams(dto)
        return ResponsePage<SysUser>(sysUserDao!!.selectGroupNoUsers(map))
    }

    fun loadGroupUsers(dto: SysUserGroupPk): ResponsePage<*> {
        PageHelper.startPage<Any>(dto.page, dto.size)
        val map = getQueryParams(dto)
        return ResponsePage<SysUser>(sysUserDao!!.selectGroupUsers(map))
    }

    private fun getQueryParams(dto: SysUserGroupPk): Map<String, Any> {
        return object : HashMap<String, Any>(1) {
            init {
                put("groupId", dto.groupId!!)
                if (StringUtils.isNotBlank(dto.name)) {
                    put("name", dto.name!!)
                }
            }
        }
    }

    fun batchInsert(list: List<SysUserGroupPk>): Int? {
        return sysUserGroupPkDao!!.insertGroupUsers(list)
    }

    fun batchDelete(groupId: Int, userIds: Array<Long>): Int? {
        val map = object : HashMap<String, Any>(2) {
            init {
                put("groupId", groupId)
                put("userIds", userIds)
            }
        }
        return sysUserGroupPkDao!!.deleteGroupUsers(map)
    }
}
