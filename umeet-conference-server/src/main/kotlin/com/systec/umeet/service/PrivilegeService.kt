package com.umeet.conference.service
import com.github.pagehelper.PageInfo
import com.umeet.conference.base.BaseServiceImpl
import com.umeet.conference.dao.PrivilegeDao
import com.umeet.conference.model.Privilege
import com.umeet.conference.model.RoleAndPrivilege
import com.umeet.conference.model.User
import com.umeet.conference.util.FastJsonUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper



@Service
class PrivilegeService : BaseServiceImpl<Privilege> (){

    @Autowired
    var privilegeDao : PrivilegeDao? = null

    @Autowired
    var roleAndPrivilegeService : RoleAndPrivilegeService ? = null

    override fun getMapper(): Mapper<Privilege> {
        return privilegeDao!!;
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: Privilege): PageInfo<Privilege> {
        var orderSqlStr = " sort "
        return super.selectPage(record.page , record.rows ,record,orderSqlStr);
    }


}
