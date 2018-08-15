package com.systec.umeet.service
import com.github.pagehelper.PageInfo
import com.systec.umeet.base.BaseServiceImpl
import com.systec.umeet.contants.ResponsePage
import com.systec.umeet.dao.SysRoleDao
import com.systec.umeet.dao.SysRolePermissionPkDao
import com.systec.umeet.model.SysRole
import com.systec.umeet.model.SysRolePermissionPk
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper

@Service
class SysRoleService : BaseServiceImpl<SysRole> (){

    @Autowired
    var sysRoleDao : SysRoleDao? = null;

    @Autowired
    var sysRolePermissionDao : SysRolePermissionPkDao ?= null

    override fun getMapper(): Mapper<SysRole> {
        return sysRoleDao!!;
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: SysRole): ResponsePage<SysRole> {
        return super.selectPage(record.page , record.size ,record)
    }

     fun deleteById(id: Any) {
        // 删除关联的权限
        val rolePermissionPk = SysRolePermissionPk()
        rolePermissionPk.roleId = Integer.valueOf(id.toString())
        sysRolePermissionDao!!.delete(rolePermissionPk)
        // 删除角色
        super.deleteByPrimaryKey(id)
    }


    fun selectUserAndGroupResultRoles(userId: Long?): List<SysRole> {
        return sysRoleDao!!.selectUserAndGroupResultRoles(userId)
    }
}
