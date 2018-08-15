package com.systec.umeet.service
import com.github.pagehelper.PageInfo
import com.systec.umeet.model.SysUser
import com.systec.umeet.base.BaseServiceImpl
import com.systec.umeet.contants.ResponsePage
import com.systec.umeet.dao.SysUserDao
import com.systec.umeet.dao.SysUserGroupDao
import com.systec.umeet.dao.SysUserGroupPkDao
import com.systec.umeet.dao.SysUserGroupRolePkDao
import com.systec.umeet.model.SysUserGroup
import com.systec.umeet.model.SysUserGroupPk
import com.systec.umeet.model.SysUserGroupRolePk
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper

@Service
class SysUserGroupService : BaseServiceImpl<SysUserGroup> (){

    @Autowired
    var sysUserGroupDao : SysUserGroupDao? = null

    @Autowired
    var sysUserGroupPkDao : SysUserGroupPkDao ?= null

    @Autowired
    var sysUserGroupRoleDao : SysUserGroupRolePkDao ?= null

    override fun getMapper(): Mapper<SysUserGroup> {
        return sysUserGroupDao!!
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: SysUserGroup): ResponsePage<SysUserGroup> {
        return super.selectPage(record.page , record.size ,record)
    }

    fun deleteById(id: Any) {
        val groupId = Integer.valueOf(id.toString())
        // 删除用户组和用户关联
        val userGroupPk = SysUserGroupPk()
        userGroupPk.groupId = groupId
        sysUserGroupPkDao!!.delete(userGroupPk)
        // 删除用户组和角色关联
        val userGroupRolePk = SysUserGroupRolePk()
        userGroupRolePk.groupId = Integer.valueOf(id.toString())
        sysUserGroupRoleDao!!.delete(userGroupRolePk)
        // 删除用户组
        super.deleteByPrimaryKey(id)
    }

}
