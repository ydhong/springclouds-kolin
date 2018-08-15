package com.systec.umeet.service
import com.github.pagehelper.PageInfo
import com.systec.umeet.base.BaseServiceImpl
import com.systec.umeet.contants.ResponsePage
import com.systec.umeet.dao.*
import com.systec.umeet.model.*
import com.systec.umeet.utills.ListUtils
import com.systec.umeet.utills.MenuTreeUtil
import com.systec.umeet.utills.SnowFlake
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
import java.util.*

@Service
class SysUserService : BaseServiceImpl<SysUser> (){

    @Autowired
    var sysUserDao : SysUserDao? = null

    @Autowired
    var sysMenuDao : SysMenuDao? = null

    @Autowired
    var sysUserGroupPKDao : SysUserGroupPkDao?= null

    @Autowired
    var sysUserRolePkDao : SysUserRolePkDao ?= null

    @Autowired
    var sysResourceDao: SysResourceDao? = null

    @Autowired
    var sysRoleDao : SysRoleDao ?= null

    override fun getMapper(): Mapper<SysUser> {
        return sysUserDao!!
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: SysUser): ResponsePage<SysUser> {
        return super.selectPage(record.page , record.size ,record)
    }


    fun selectUserGroupInfo(id: Int): SysUserInfo? {
        return sysUserDao!!.selectUserGroup(id)
    }

    fun selectUserMenuTree(userId: Int?): List<MenuTree> {
        val allList:List<MenuTree> = sysMenuDao!!.selectByAll(1)
        val currentList:List<MenuTree> = sysMenuDao!!.selectUserMenus(userId)
        return MenuTreeUtil.filterGenerateSortMenu(allList, currentList)
    }

    fun selectUserAllResourceCodes(userId: Int?): Any? {
        return sysResourceDao!!.selectUserAllResourceCodes(userId)
    }

    fun selectUserResourceCodes(map: HashMap<String, Any>): Any? {
        return sysResourceDao!!.selectUserResourceCodes(map)
    }

    /**
     * 保存用户信息
     * @param dto
     */
    override fun save(sysUser: SysUser): Int {
        // 保存用户
        val currentTime = System.currentTimeMillis()
        //sysUser.id = SnowFlake.getInstance().nextId()
        sysUser.createTime = currentTime
        sysUser.lastPasswordChange = currentTime
        super.insert(sysUser)
        // 添加用户与组之间的关联
        val pk = SysUserGroupPk()
        pk.userId = sysUser.id
        pk.groupId = sysUser.groupId
        return sysUserGroupPKDao!!.insert(pk)
    }

    /**
     * 修改用户信息
     * @param dto
     */
    fun update(dto: HashMap<String,Any>) {
        val userId = dto["id"]
        val newGroupId = dto["groupId"]

        // 判断是否修改用户组
        val pk = SysUserGroupPk()
        pk.userId = dto["id"] as Int?
        val newPk = this.sysUserGroupPKDao!!.selectOne(pk)
        if (newPk == null) {
            pk.groupId = newGroupId as Int?
            this.sysUserGroupPKDao!!.insert(pk)
        } else {
            val originalGroupId = newPk!!.groupId!!
            if (originalGroupId != newGroupId) {
                val map = object : HashMap<String, Any>(3) {
                    init {
                        put("originalGroupId", originalGroupId!!)
                        put("newGroupId", newGroupId!!)
                        put("userId", userId!!)
                    }
                }
                this.sysUserGroupPKDao!!.updateGroupUser(map)
            }
        }

        val sysUser = super.selectByPrimaryKey(userId!!)
        sysUser.username = dto["username"].toString()
        sysUser.name = dto["name"].toString()
        sysUser.state = dto["state"] as String?
        this.sysUserDao!!.updateByPrimaryKey(sysUser)
    }

    /**
     * 删除用户，在删除用户的同时，需要先删除用户和角色还有用户组之间的关联关系
     * @param id 用户ID
     */
    fun delete(id: Int?) {
        // 删除用户和角色的关联
        val userRolePk = SysUserRolePk()
        userRolePk.userId = id
        this.sysUserRolePkDao!!.delete(userRolePk)
        // 删除和用户组之间的关联
        val userGroupPk = SysUserGroupPk()
        userGroupPk.userId = id
        this.sysUserGroupPKDao!!.delete(userGroupPk)
        // 删除用户
        super.deleteByPrimaryKey(id!!)
    }

    /**
     * 查询用户信息(包括组)
     * @param userId
     * @return
     */
    fun selectUserGroupInfo(userId: Int?): SysUserInfo {
        return sysUserDao!!.selectUserGroup(userId)
    }

    /**
     * 查询用户明细信息(包括组，角色，权限，资源等)
     * @param userId
     * @return
     */
    fun selectUserDetail(userId: Int?): HashMap<String,Any> {
        val userMap = object : HashMap<String, Any>(1) {
            init {
                put("userId", userId!!)
            }
        }
        var roleList: MutableList<SysRole> = ArrayList<SysRole>()
        val userRoles = sysRoleDao!!.selectUserRoles(userMap)
        if (!ListUtils.isEmpty(userRoles)) {
            roleList.addAll(userRoles)
        }
        val userInfo = this.selectUserGroupInfo(userId!!)
        if (!StringUtils.isEmpty(userInfo!!.name)) {
            val groupMap = object : HashMap<String, Any>(1) {
                init {
                    put("groupId", userInfo!!.groupId!!)
                }
            }
            val groupRoles = sysRoleDao!!.selectUserGroupRoles(groupMap)
            if (!ListUtils.isEmpty(groupRoles)) {
                roleList.addAll(groupRoles)
            }
        }
        val userDetail = HashMap<String,Any>()
        userDetail["userGroupInfo"] = userInfo
        // 用户角色和用户组角色 合并后角色去重
        if (!ListUtils.isEmpty(roleList)) {
            val roleCodes = ArrayList<String>()
            roleList.forEach { item -> roleCodes.add(item.code!!) }
            userDetail["roleCodes"] = roleCodes
            userDetail["roleList"] = roleList
        }
        userDetail["resourceList"] = sysResourceDao!!.selectUserResources(userId)
        return userDetail
    }

    /**
     * 查询用户管理管理的所有资源
     * @param userId
     * @return
     */
    fun selectUserResources(userId: Int?): SysUser? {
        val sysUser = sysUserDao!!.selectByPrimaryKey(userId)
        if (sysUser != null) {
            sysUser!!.resourceList = this.sysResourceDao!!.selectUserResources(sysUser!!.id)
        }
        return sysUser
    }


}
