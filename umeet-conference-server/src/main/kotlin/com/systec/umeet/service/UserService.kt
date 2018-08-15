package com.umeet.conference.service
import com.github.pagehelper.PageInfo
import com.umeet.conference.base.BaseServiceImpl
import com.umeet.conference.contants.Condition
import com.umeet.conference.contants.ConditionType
import com.umeet.conference.dao.UserDao
import com.umeet.conference.model.Organization
import com.umeet.conference.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
import java.util.*

@Service
class UserService : BaseServiceImpl<User> (){

    @Autowired
    var userDao : UserDao? = null

    override fun getMapper(): Mapper<User> {
        return userDao!!
    }

    @Autowired
    var adDataService :ADDataService ?=null

    @Autowired
    var organizationService:OrganizationService ?= null

    @Autowired
    var globalConfigurService :GlobalConfigurService ?= null

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: User): PageInfo<User> {
        var orderSqlStr = " sort "
        return super.selectPage(record.page , record.rows ,record,orderSqlStr);
    }

    fun synchronizeADUsers() {
        val map = globalConfigurService!!.readData()
        val passWord = if (map.get("adPassword") == null) "" else map.get("adPassword").toString()
        val userName = if (map.get("adUsername") == null) "" else map.get("adUsername").toString()
        val isADGroup = if (map.get("isADGroup") == null) "" else map.get("isADGroup").toString()
        val isEnableAD = if (map.get("isEnableAD") == null) "" else map.get("isEnableAD").toString()
       // adDataService!!.syncOffice(userName,passWord)
        adDataService!!.syncUsers(userName,passWord)
    }

    fun query(record: User): Any? {
        var type = globalConfigurService!!.getUserType()
        var orgId = record.orgId
        var conditions : MutableList<Condition> = ArrayList<Condition>()
        var orgIds :MutableList<String> = ArrayList<String>()
        if(orgId != null){
            var parent = organizationService!!.selectByPrimaryKey(orgId)
            var orderconditions : MutableList<Condition> = ArrayList<Condition>()
            var condition  = Condition()
            condition!!.key = "levels"
            condition!!.type = ConditionType.LIKE
            condition!!.value = "%" + parent.levels + "%"

            var condition2  = Condition()
            condition2!!.key = "type"
            condition2!!.type = ConditionType.LIKE
            condition2!!.value =  type

            orderconditions.add(condition)
            orderconditions.add(condition2)
            orgIds!!.add(orgId!!)
            var orgs = organizationService!!.selectByCodition(Organization(),orderconditions," sort ")
            if(orgs != null && orgs.size > 0){
                for (organization:Organization in orgs){
                    orgIds!!.add(organization.id!!)
                }
            }
        }

        if(record.isEnable != null) {
            var condition  = Condition()
            condition!!.key = "isEnable"
            condition!!.type = ConditionType.EQUAL
            condition!!.value = record.isEnable.toString()
            conditions.add(condition)
        }
        if(record.roleId != null && record.roleId!!.isNotEmpty()) {
            var condition  = Condition()
            condition!!.key = "roleId"
            condition!!.type = ConditionType.EQUAL
            condition!!.value = record.roleId
            conditions.add(condition)
        }
        if(type != null && type!!.isNotEmpty()) {
            var condition  = Condition()
            condition!!.key = "type"
            condition!!.type = ConditionType.EQUAL
            condition!!.value = type
            conditions.add(condition)
        }
        if(orgIds != null && orgIds.size > 0) {
            var condition  = Condition()
            condition!!.key = "orgId"
            condition!!.type = ConditionType.IN
            condition!!.values = orgIds
            conditions.add(condition)
        }
        var orderSql = " sort "
        return super.selectPageByCondicion(record.page,record.rows,record, conditions!!,orderSql)
    }

    fun enableUserByType(type: String) {
        userDao!!.enableUserByType(type)
    }

    fun disableUserByType(type: String) {
        userDao!!.disableUserByType(type)
    }

    fun queryOrgUserByAccount(account: String,type: String): List<User> {
        var record = User()
        record.account = account
        record.type = type
        return super.select(record)
    }

    fun queryUserSrotMax(): Int {
        var user = userDao!!.queryUserMaxSort()
        var sort = 0
        if(user !=null && user.sort != null) sort= user.sort!!
        return sort
    }
}
