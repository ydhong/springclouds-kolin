package com.umeet.conference.service
import com.github.pagehelper.PageInfo
import com.umeet.conference.base.BaseServiceImpl
import com.umeet.conference.dao.OrganizationDao
import com.umeet.conference.dao.UserDao
import com.umeet.conference.model.Organization
import com.umeet.conference.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper

@Service
class OrganizationService : BaseServiceImpl<Organization> (){

    @Autowired
    var organizationDao : OrganizationDao? = null;

    override fun getMapper(): Mapper<Organization> {
        return organizationDao!!;
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: Organization): PageInfo<Organization> {
        var orderSqlStr = " sort "
        return super.selectPage(record.page , record.rows ,record,orderSqlStr);
    }
}
