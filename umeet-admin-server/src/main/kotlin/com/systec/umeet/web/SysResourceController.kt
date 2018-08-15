package com.systec.umeet.web

import com.systec.umeet.contants.HttpResponse
import com.systec.umeet.contants.ResourcesMapping
import com.systec.umeet.contants.SysConstant
import com.systec.umeet.model.SysResource
import com.systec.umeet.service.SysResourceService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Api(value = "资源管理", tags = arrayOf("资源管理接口"))
@RestController
@RequestMapping(value = "api/sysResource")
class SysResourceController {

    @Autowired
    var sysResourceService : SysResourceService? = null;

    @ApiOperation(value = "资源添加", notes = "根据SysResource对象增加资源")
    @ResourcesMapping(element = "添加", code = "sys_resource_add")
    @PostMapping
    fun add(@Validated @RequestBody sysResource: SysResource, bindingResult: BindingResult): HttpResponse<*> {
        if (bindingResult.hasErrors()) {
            return HttpResponse.errorParams()
        }
        sysResourceService!!.insert(sysResource)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "资源删除", notes = "根据资源id删除资源信息")
    @ResourcesMapping(element = "删除", code = "sys_resource_delete")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): HttpResponse<*> {
        sysResourceService!!.deleteByPrimaryKey(id)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "资源查询", notes = "可分页并可根据权限名称模糊检索")
    @ResourcesMapping(element = "查询", code = "sys_resource_query")
    @GetMapping
    fun query(queryDto: SysResource): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysResourceService!!.getPage(queryDto))
    }

    @ApiOperation(value = "资源修改", notes = "根据传递的SysPermission对象来更新, SysPermission对象必须包含id")
    @ResourcesMapping(element = "修改", code = "sys_resource_update")
    @PutMapping
    fun update(@Validated @RequestBody sysResource: SysResource, bindingResult: BindingResult): HttpResponse<*> {
        if (bindingResult.hasErrors()) {
            return HttpResponse.errorParams()
        }
        sysResourceService!!.update(sysResource)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "资源详情查询", notes = "根据id查询资源详细信息")
    @ResourcesMapping(element = "详情查询", code = "sys_resource_info")
    @GetMapping("/{id}")
    fun info(@PathVariable id: Long?): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysResourceService!!.selectByPrimaryKey(id!!))
    }

    @ApiOperation(value = "资源树查询", notes = "查询所有的资源并返回父子的树状结构")
    @ResourcesMapping(element = "查询", code = "sys_resource_tree")
    @GetMapping("/tree")
    fun tree(): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysResourceService!!.selectSysResourceTree())
    }

    @ApiOperation(value = "资源是否已存在", notes = "根据SysResource对象设定的字段值来查询判断")
    @GetMapping(SysConstant.API_NO_PERMISSION + "exist")
    fun exist(resource: SysResource?): HttpResponse<*> {
        return if (resource == null) {
            HttpResponse.errorParams()
        } else HttpResponse.resultSuccess(sysResourceService!!.selectOne(resource) != null)
    }
}
