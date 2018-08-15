package com.systec.umeet.dao

import com.systec.umeet.model.MenuTree
import com.systec.umeet.model.SysMenu
import com.systec.umeet.utills.MyMapper

interface SysMenuDao :MyMapper<SysMenu>{

    /**
     * 查询所有可用的菜单
     * @param state
     * @return
     */
    abstract fun selectByAll(state: Int?): List<MenuTree>

    /**
     * 根据权限加载已分配的菜单列表
     * @param permissionId
     * @return
     */
    abstract fun selectPermissionMenus(permissionId: Int?): List<MenuTree>

    /**
     * 加载未分配给当前权限的菜单列表
     * @param permissionId
     * @return
     */
    abstract fun selectPermissionNoExistMenus(permissionId: Int?): List<MenuTree>

    /**
     * 查询用户关联角色关联权限关联的菜单
     * @param userId
     * @return
     */
    abstract fun selectUserMenus(userId: Int?): List<MenuTree>
}
