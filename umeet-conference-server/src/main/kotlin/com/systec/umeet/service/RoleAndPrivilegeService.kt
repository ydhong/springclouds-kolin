package com.umeet.conference.service
import com.github.pagehelper.PageInfo
import com.umeet.conference.base.BaseServiceImpl
import com.umeet.conference.dao.RoleAndPrivilegeDao
import com.umeet.conference.model.RoleAndPrivilege
import com.umeet.conference.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper

@Service
class RoleAndPrivilegeService : BaseServiceImpl<RoleAndPrivilege> (){

    @Autowired
    var roleAndPrivilegeDao : RoleAndPrivilegeDao? = null;

    override fun getMapper(): Mapper<RoleAndPrivilege> {
        return roleAndPrivilegeDao!!;
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: RoleAndPrivilege): PageInfo<RoleAndPrivilege> {
        var orderSqlStr = " sort "
        return super.selectPage(record.page , record.rows ,record,orderSqlStr);
    }

    fun getPrivatesByUser(user: User): Any? {
        var record = RoleAndPrivilege()
        record!!.roleId = user.roleId
        val roleAndPrivileges:List<RoleAndPrivilege> = super.select(record)
        return roleAndPrivileges
    }
}
