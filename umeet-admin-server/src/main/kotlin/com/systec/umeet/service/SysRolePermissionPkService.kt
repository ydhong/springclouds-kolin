package com.systec.umeet.service
import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import com.systec.umeet.base.BaseServiceImpl
import com.systec.umeet.contants.ResponsePage
import com.systec.umeet.dao.SysPermissionDao
import com.systec.umeet.dao.SysRolePermissionPkDao
import com.systec.umeet.model.SysPermission
import com.systec.umeet.model.SysRolePermissionPk
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
import java.util.*

@Service
class SysRolePermissionPkService : BaseServiceImpl<SysRolePermissionPk> (){

    @Autowired
    var sysRolePermissionPkDao : SysRolePermissionPkDao? = null

    @Autowired
    var sysPermissionDao :SysPermissionDao ?= null

    override fun getMapper(): Mapper<SysRolePermissionPk> {
        return sysRolePermissionPkDao!!
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: SysRolePermissionPk): ResponsePage<SysRolePermissionPk> {
        return super.selectPage(record.page , record.size ,record)
    }

    fun loadRoleNoRelationPermissions(dto: SysRolePermissionPk): ResponsePage<*> {
        PageHelper.startPage<Any>(dto.page, dto.size)
        val map = getQueryParams(dto)
        return ResponsePage<SysPermission>(sysPermissionDao!!.selectRoleNoPermissions(map))
    }

    fun loadRolePermissions(dto: SysRolePermissionPk): ResponsePage<*> {
        PageHelper.startPage<Any>(dto.page, dto.size)
        val map = getQueryParams(dto)
        return ResponsePage<SysPermission>(sysPermissionDao!!.selectRolePermissions(map))
    }

    private fun getQueryParams(dto: SysRolePermissionPk): Map<String, Any> {
        return object : HashMap<String, Any>(1) {
            init {
                put("roleId", dto.roleId!!)
                if (StringUtils.isNotBlank(dto.name)) {
                    put("name", dto.name!!)
                }
            }
        }
    }

    fun batchInsert(list: List<SysRolePermissionPk>): Int? {
        return sysRolePermissionPkDao!!.insertRolePermissions(list)
    }

    fun batchDelete(roleId: Int?, permissionIds: Array<Int>): Int? {
        val map = object : HashMap<String, Any>(2) {
            init {
                put("roleId", roleId!!)
                put("permissionIds", permissionIds)
            }
        }
        return sysRolePermissionPkDao!!.deleteRolePermissions(map)
    }
}
