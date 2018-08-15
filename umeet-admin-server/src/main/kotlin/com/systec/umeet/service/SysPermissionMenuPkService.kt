package com.systec.umeet.service
import com.github.pagehelper.PageInfo
import com.systec.umeet.base.BaseServiceImpl
import com.systec.umeet.contants.ResponsePage
import com.systec.umeet.dao.SysMenuDao
import com.systec.umeet.dao.SysPermissionMenuPkDao
import com.systec.umeet.model.MenuTree
import com.systec.umeet.model.SysPermissionMenuPk
import com.systec.umeet.utills.MenuTreeUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
import java.util.*

@Service
class SysPermissionMenuPkService : BaseServiceImpl<SysPermissionMenuPk> (){

    @Autowired
    var sysPermissionMenuPkDao : SysPermissionMenuPkDao? = null

    @Autowired
    var sysMenuDao :SysMenuDao? = null

    override fun getMapper(): Mapper<SysPermissionMenuPk> {
        return sysPermissionMenuPkDao!!;
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: SysPermissionMenuPk): ResponsePage<SysPermissionMenuPk> {
        return super.selectPage(record.page , record.size ,record)
    }

    fun loadPermissionNoMenuTree(permissionId: Int?): List<MenuTree>? {
        val allList = sysMenuDao!!.selectByAll(1)
        val currentList = sysMenuDao!!.selectPermissionNoExistMenus(permissionId)
        return MenuTreeUtil.filterGenerateSortMenu(allList, currentList)
    }

    fun loadPermissionMenuTree(permissionId: Int?): List<MenuTree>? {
        val allList = sysMenuDao!!.selectByAll(1)
        val currentList = sysMenuDao!!.selectPermissionMenus(permissionId)
        return MenuTreeUtil.filterGenerateSortMenu(allList, currentList)
    }

    fun batchInsert(list: List<SysPermissionMenuPk>): Int? {
        return sysPermissionMenuPkDao!!.insertPermissionMenus(list)
    }

    fun batchDelete(permissionId: Int?, menuIds: Array<Int>): Int? {
        val map = object : HashMap<String, Any>(2) {
            init {
                put("permissionId", permissionId!!)
                put("menuIds", menuIds)
            }
        }
        return sysPermissionMenuPkDao!!.deletePermissionMenus(map)
    }

}
