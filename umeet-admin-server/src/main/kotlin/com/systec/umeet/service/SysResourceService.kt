package com.systec.umeet.service
import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import com.systec.umeet.base.BaseServiceImpl
import com.systec.umeet.contants.ResponsePage
import com.systec.umeet.dao.SysResourceDao
import com.systec.umeet.model.SysResource
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
import tk.mybatis.mapper.entity.Example
import java.util.*

@Service
class SysResourceService : BaseServiceImpl<SysResource> (){

    @Autowired
    var sysResourcenDao : SysResourceDao? = null

    override fun getMapper(): Mapper<SysResource> {
        return sysResourcenDao!!
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: SysResource): ResponsePage<SysResource> {
        return super.selectPage(record.page , record.size ,record)
    }

    fun findByPageList(dto: HashMap<String,Any>): ResponsePage<SysResource> {
        PageHelper.startPage<Any>(dto["page"] as Int, dto["size"] as Int)
        val example = Example(SysResource::class.java)
        val criteria = example.createCriteria()
        if (dto["type"] != 0) {
            criteria.andCondition("type=", dto["type"])
        }
        val list = sysResourcenDao!!.selectByExample(example)
        return ResponsePage<SysResource>(list)
    }

    fun selectSysResourceTree(): List<SysResource> {
        val allSysResources = sysResourcenDao!!.selectAllResources()
        val sysResources = ArrayList<SysResource>()
        var parent: SysResource? = null
        for (i in allSysResources.indices) {
            val item = allSysResources.get(i)
            if (item.level == 1) {
                sysResources.add(item)
            } else {
                if (parent == null || item.parentId != parent.id) {
                    for (j in sysResources.indices) {
                        val parentItem = sysResources[j]
                        if (item.parentId == parentItem.id) {
                            parent = parentItem
                            parent.childrens = ArrayList<SysResource>()
                            break
                        }
                    }
                }
                parent!!.childrens!!.add(item)
            }
        }
        return sysResources
    }

    fun selectUserRolePermissionResource(map: Map<String, Any>): SysResource {
        return sysResourcenDao!!.selectUserRolePermissionResource(map)
    }

}
