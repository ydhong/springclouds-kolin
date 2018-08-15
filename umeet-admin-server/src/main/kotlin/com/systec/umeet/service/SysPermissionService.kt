package com.systec.umeet.service
import com.github.pagehelper.PageInfo
import com.systec.umeet.model.SysPermission
import com.systec.umeet.base.BaseServiceImpl
import com.systec.umeet.contants.ResponsePage
import com.systec.umeet.dao.SysPermissionDao
import com.systec.umeet.dao.SysPermissionMenuPkDao
import com.systec.umeet.dao.SysPermissionResourcePkDao
import com.systec.umeet.dao.SysResourceDao
import com.systec.umeet.model.SysPermissionMenuPk
import com.systec.umeet.model.SysPermissionResourcePk
import com.systec.umeet.model.SysResource
import com.systec.umeet.utills.ListUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
import java.util.*

@Service
class SysPermissionService : BaseServiceImpl<SysPermission> (){

    @Autowired
    var sysPermissionDao : SysPermissionDao? = null

    @Autowired
    private val sysPermissionResourcePkDao: SysPermissionResourcePkDao? = null

    @Autowired
    private val sysResourceDao: SysResourceDao? = null

    @Autowired
    var sysPermissionMenuPkDao : SysPermissionMenuPkDao ?= null

    override fun getMapper(): Mapper<SysPermission> {
        return sysPermissionDao!!
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: SysPermission): ResponsePage<SysPermission> {
        return super.selectPage(record.page , record.size ,record)
    }



    fun delete(id: Int) {
        // 删除关联菜单
        val permissionMenuPk = SysPermissionMenuPk()
        permissionMenuPk.permissionId = id
        sysPermissionMenuPkDao!!.delete(permissionMenuPk)
        // 删除关联资源
        val permissionResourcePk = SysPermissionResourcePk()
        permissionResourcePk.permissionId = id
        sysPermissionResourcePkDao!!.delete(permissionResourcePk)
        // 删除权限
        val sysPermission = SysPermission()
        sysPermission.id = id
        sysPermissionDao!!.delete(sysPermission)
    }

    fun selectPermissionResource(permissionId: Int): List<SysResource> {
        val allSysResources = sysResourceDao!!.selectAllResources()
        val currentSysResources = sysResourceDao!!.selectPermissionResources(permissionId)
        val resourcesIds = ArrayList<Long>()
        if (!ListUtils.isEmpty(currentSysResources)) {
            currentSysResources.forEach { item -> resourcesIds.add(item.id!!) }
        }
        val sysResources = ArrayList<SysResource>()
        var parent: SysResource? = null
        for (i in allSysResources.indices) {
            val item = allSysResources.get(i)
            if (resourcesIds.size > 0 && resourcesIds.contains(item.id!!)) {
                item.checked = true
            }
            if (item.level == 1) {
                sysResources.add(item)
            } else {
                if (parent == null || item.parentId != parent!!.id) {
                    for (j in sysResources.indices) {
                        val parentItem = sysResources[j]
                        if (item.parentId == parentItem.id) {
                            parent = parentItem
                            parent!!.childrens = ArrayList<SysResource>()
                            break
                        }
                    }
                }
                parent!!.childrens!!.add(item)
            }
        }
        return sysResources
    }

    fun batchInsertPermissionResource(list: List<SysPermissionResourcePk>): Int? {
        val pk = SysPermissionResourcePk()
        pk.permissionId = list[0].permissionId
        sysPermissionResourcePkDao!!.delete(pk)
        return sysPermissionResourcePkDao!!.insertPermissionResources(list)
    }
}
