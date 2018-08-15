package com.systec.umeet.service
import com.github.pagehelper.PageInfo
import com.systec.umeet.base.BaseServiceImpl
import com.systec.umeet.contants.ResponsePage
import com.systec.umeet.dao.SysPermissionResourcePkDao
import com.systec.umeet.model.SysPermissionResourcePk
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
import java.util.*

@Service
class SysPermissionResourceService : BaseServiceImpl<SysPermissionResourcePk> (){

    @Autowired
    var sysPermissionResourcePkDao : SysPermissionResourcePkDao? = null;

    override fun getMapper(): Mapper<SysPermissionResourcePk> {
        return sysPermissionResourcePkDao!!
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: SysPermissionResourcePk): ResponsePage<SysPermissionResourcePk> {
        return super.selectPage(record.page , record.size ,record)
    }


    private fun getQueryParams(dto: SysPermissionResourcePk): Map<String, Any> {
        return object : HashMap<String, Any>(1) {
            init {
                put("permissionId", dto.permissionId!!)
                if (StringUtils.isNotBlank(dto.name)) {
                    put("name", dto.name!!)
                }
            }
        }
    }

    fun batchInsert(list: List<SysPermissionResourcePk>): Int? {
        return sysPermissionResourcePkDao!!.insertPermissionResources(list)
    }

    fun batchDelete(permissionId: Int?, resourceIds: Array<Long>): Int? {
        val map = object : HashMap<String, Any>(2) {
            init {
                put("permissionId", permissionId!!)
                put("resourceIds", resourceIds)
            }
        }
        return sysPermissionResourcePkDao!!.deletePermissionResources(map)
    }
}
