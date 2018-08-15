package com.systec.umeet.service
import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import com.systec.umeet.base.BaseServiceImpl
import com.systec.umeet.contants.ResponsePage
import com.systec.umeet.dao.SysRoleDao
import com.systec.umeet.dao.SysUserRolePkDao
import com.systec.umeet.model.SysRole
import com.systec.umeet.model.SysUserRolePk
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
import java.util.*

@Service
class SysUserRolePkService : BaseServiceImpl<SysUserRolePk> (){

    @Autowired
    var sysUserRolePkDao : SysUserRolePkDao? = null

    override fun getMapper(): Mapper<SysUserRolePk> {
        return sysUserRolePkDao!!
    }

    @Autowired
    var sysRoleDao:SysRoleDao? = null


    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: SysUserRolePk): ResponsePage<SysUserRolePk> {
        return super.selectPage(record.page , record.size ,record)
    }

    fun loadUserNoRelationRoles(dto: SysUserRolePk): ResponsePage<SysRole> {
        PageHelper.startPage<Any>(dto.page, dto.size)
        val map = getQueryParams(dto)
        var roles = sysRoleDao!!.selectUserNoRoles(map)
        var page = ResponsePage<SysRole>(roles)
        return page
    }

    fun loadUserRoles(dto: SysUserRolePk): ResponsePage<*> {
        PageHelper.startPage<Any>(dto.page, dto.size)
        val map = getQueryParams(dto)
        return ResponsePage<SysRole>(sysRoleDao!!.selectUserRoles(map))
    }

    private fun getQueryParams(dto: SysUserRolePk): Map<String, Any> {
        return object : HashMap<String, Any>(1) {
            init {
                put("userId", dto.userId!!)
            }
        }
    }

    fun batchInsert(list: List<SysUserRolePk>): Int? {
        return sysUserRolePkDao!!.insertUserRoles(list)
    }

    fun batchDelete(map: HashMap<String, Any>): Int? {
        return sysUserRolePkDao!!.deleteUserRoles(map)
    }

}
