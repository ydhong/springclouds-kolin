package com.umeet.conference.service
import com.github.pagehelper.PageInfo
import com.umeet.conference.base.BaseServiceImpl
import com.umeet.conference.dao.RoleDao
import com.umeet.conference.model.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper

@Service
class RoleService : BaseServiceImpl<Role> (){

    @Autowired
    var roleDao : RoleDao? = null;

    override fun getMapper(): Mapper<Role> {
        return roleDao!!;
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: Role): PageInfo<Role> {
        return super.selectPage(record.page , record.rows ,record)
    }

    fun getRoleById(roleId: Int?): Any {
        var record : Role? = null
        record!!.id = roleId.toString()

        var roles = super.select(record)

        return roles
    }
}
