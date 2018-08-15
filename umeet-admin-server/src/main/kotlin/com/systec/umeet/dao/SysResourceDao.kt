package com.systec.umeet.dao

import com.systec.umeet.model.SysResource
import com.systec.umeet.utills.MyMapper

interface SysResourceDao :MyMapper<SysResource>{
    /**
     * 查询某个权限关联的资源
     * @param permissionId
     * @return
     */
    abstract fun selectPermissionResources(permissionId: Int?): List<SysResource>

    /**
     * 查询某个用户的资源
     * @param userId
     * @return
     */
    abstract fun selectUserResources(userId: Int?): List<SysResource>

    /**
     * 查询所有资源
     * @return
     */
    abstract fun selectAllResources(): List<SysResource>

    /**
     * 查询用户访问的某个资源是否被关联
     * @param map
     * @return
     */
    abstract fun selectUserRolePermissionResource(map: Map<String, Any>): SysResource

    /**
     * 查询用户所有的资源编码
     * @param userId
     * @return
     */
    abstract fun selectUserAllResourceCodes(userId: Int?): List<String>

    /**
     * 查询用户下的一个或多个资源编码
     * @param map
     * @return
     */
    abstract fun selectUserResourceCodes(map: Map<String, Any>): List<String>

}
