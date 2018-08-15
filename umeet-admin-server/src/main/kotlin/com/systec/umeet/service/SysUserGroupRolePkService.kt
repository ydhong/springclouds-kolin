package com.systec.umeet.service
import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import com.systec.umeet.base.BaseServiceImpl
import com.systec.umeet.contants.ResponsePage
import com.systec.umeet.dao.SysRoleDao
import com.systec.umeet.dao.SysUserGroupRolePkDao
import com.systec.umeet.model.SysRole
import com.systec.umeet.model.SysUserGroupRolePk
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
import java.util.*

@Service
class SysUserGroupRolePkService : BaseServiceImpl<SysUserGroupRolePk> (){

    @Autowired
    var sysUserRoleRolePkDao : SysUserGroupRolePkDao? = null

    @Autowired
    var sysRoleDao : SysRoleDao ?= null

    override fun getMapper(): Mapper<SysUserGroupRolePk> {
        return sysUserRoleRolePkDao!!
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: SysUserGroupRolePk): ResponsePage<SysUserGroupRolePk> {
        return super.selectPage(record.page , record.size ,record)
    }

    fun loadGroupNoRelationRoles(dto: SysUserGroupRolePk): ResponsePage<*> {
        PageHelper.startPage<Any>(dto.page, dto.size)
        val map = getQueryParams(dto)
        return ResponsePage<SysRole>(sysRoleDao!!.selectUserGroupNoRoles(map))
    }

    fun loadGroupRoles(dto: SysUserGroupRolePk): ResponsePage<*> {
        PageHelper.startPage<Any>(dto.page, dto.size)
        val map = getQueryParams(dto)
        return ResponsePage<SysRole>(sysRoleDao!!.selectUserGroupRoles(map))
    }

    private fun getQueryParams(dto: SysUserGroupRolePk): Map<String, Any> {
        return object : HashMap<String, Any>(1) {
            init {
                put("groupId", dto.groupId!!)
                if (StringUtils.isNotBlank(dto.name)) {
                    put("name", dto.name!!)
                }
            }
        }
    }

    fun batchInsert(list: List<SysUserGroupRolePk>): Int? {
        return sysUserRoleRolePkDao!!.insertGroupRoles(list)
    }

    fun batchDelete(groupId: Int, roleIds: IntArray): Int? {
        val map = object : HashMap<String, Any>(2) {
            init {
                put("groupId", groupId)
                put("roleIds", roleIds)
            }
        }
        return sysUserRoleRolePkDao!!.deleteGroupRoles(map)
    }
}
