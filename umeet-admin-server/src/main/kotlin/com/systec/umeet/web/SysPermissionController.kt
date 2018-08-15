package com.systec.umeet.web

import com.systec.umeet.contants.HttpResponse
import com.systec.umeet.contants.ResourcesMapping
import com.systec.umeet.contants.SysConstant
import com.systec.umeet.model.SysPermission
import com.systec.umeet.model.SysPermissionMenuPk
import com.systec.umeet.model.SysPermissionResourcePk
import com.systec.umeet.service.SysPermissionMenuPkService
import com.systec.umeet.service.SysPermissionService
import com.systec.umeet.utills.ListUtils
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@Api(value = "权限管理", tags = arrayOf("权限管理接口"))
@RestController
@RequestMapping(value = "api/sysPermission")
class SysPermissionController {

    @Autowired
    var sysPermissionService : SysPermissionService? = null

    @Autowired
     var sysPermissionMenuPkService: SysPermissionMenuPkService? = null

    @ApiOperation(value = "权限添加", notes = "根据SysPermission对象创建权限")
    @ResourcesMapping(element = "添加", code = "sys_permission_add")
    @PostMapping
    fun add(@RequestBody sysPermission: SysPermission, bindingResult: BindingResult): HttpResponse<*> {
        if (bindingResult.hasErrors()) {
            return HttpResponse.errorParams()
        }
        sysPermissionService!!.insert(sysPermission)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "权限删除", notes = "根据权限id删除权限信息")
    @ResourcesMapping(element = "删除", code = "sys_permission_delete")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): HttpResponse<*> {
        sysPermissionService!!.delete(id)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "权限查询", notes = "可分页并可根据权限名称模糊检索")
    @ResourcesMapping(element = "查询", code = "sys_permission_query")
    @GetMapping
    fun query(requestDto: SysPermission): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysPermissionService!!.getPage(requestDto))
    }

    @ApiOperation(value = "权限修改", notes = "根据传递的SysPermission对象来更新, SysPermission对象必须包含id")
    @ResourcesMapping(element = "修改", code = "sys_permission_update")
    @PutMapping
    fun update(@Validated @RequestBody sysPermission: SysPermission, bindingResult: BindingResult): HttpResponse<*> {
        if (bindingResult.hasErrors()) {
            return HttpResponse.errorParams()
        }
        sysPermissionService!!.update(sysPermission)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "权限详情查询", notes = "根据id查询权限详细信息")
    @ResourcesMapping(element = "详情", code = "sys_permission_info")
    @GetMapping("/{id}")
    fun info(@PathVariable id: Int): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysPermissionService!!.selectByPrimaryKey(id))
    }

    @ApiOperation(value = "权限关联菜单批量添加", notes = "保存多个SysPermissionMenuPk对象")
    @ResourcesMapping(element = "批量添加", code = "sys_permission_menu_add")
    @PostMapping("/batchMenuAdd")
    fun batchMenuAdd(@RequestBody list: kotlin.collections.List<SysPermissionMenuPk>?): HttpResponse<*> {
        if (list == null || list.size == 0) {
            return HttpResponse.errorParams()
        }
        sysPermissionMenuPkService!!.batchInsert(list)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "权限关联菜单批量删除", notes = "根据权限id和多个菜单id删除关联")
    @ResourcesMapping(element = "批量删除", code = "sys_permission_menu_delete")
    @PostMapping("/batchMenuDelete")
    fun batchMenuDelete(@Validated @RequestBody dto: HashMap<String,Any>): HttpResponse<*> {
        if (dto["menuId"] == null) {
            return HttpResponse.errorParams()
        }
        sysPermissionMenuPkService!!.batchDelete(dto["permissionId"] as Int?, dto["menuIds"] as Array<Int>)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "权限未关联菜单树查询", notes = "根据权限id查询该权限未关联的菜单并返回菜单树")
    @ResourcesMapping(element = "未关联查询", code = "sys_permission_no_menu_tree_get")
    @GetMapping("/permissionNoRelationMenuTree")
    fun permissionNoRelationMenuTree(@RequestParam permissionId: Int?): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysPermissionMenuPkService!!.loadPermissionNoMenuTree(permissionId))
    }

    @ApiOperation(value = "权限已关联菜单树查询", notes = "根据权限id查询该权限已关联的菜单并返回菜单树")
    @ResourcesMapping(element = "已关联查询", code = "sys_permission_menu_tree_get")
    @GetMapping("/permissionMenuTree")
    fun permissionMenuTree(@RequestParam permissionId: Int?): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysPermissionMenuPkService!!.loadPermissionMenuTree(permissionId))
    }

    @ApiOperation(value = "权限关联资源批量添加", notes = "添加多个SysPermissionResourcePk对象")
    @ResourcesMapping(element = "批量添加", code = "sys_permission_resource_add")
    @PostMapping("/batchResourceAdd")
    fun batchResourceAdd(@RequestBody list: kotlin.collections.List<SysPermissionResourcePk>): HttpResponse<*> {
        if (ListUtils.isEmpty(list)) {
            return HttpResponse.errorParams()
        }
        sysPermissionService!!.batchInsertPermissionResource(list)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "权限关联资源查询", notes = "根据权限id查询该权限已关联的资源并返回资源树")
    @ResourcesMapping(element = "已关联查询", code = "sys_permission_resource")
    @GetMapping("/permissionResource")
    fun permissionResource(dto: SysPermissionResourcePk): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysPermissionService!!.selectPermissionResource(dto.permissionId!!))
    }

    @ApiOperation(value = "权限是否已存在", notes = "根据SysPermission对象设定的字段值来查询判断")
    @GetMapping(SysConstant.API_NO_PERMISSION + "exist")
    fun exist(sysPermission: SysPermission?): HttpResponse<*> {
        return if (sysPermission == null) {
            HttpResponse.errorParams()
        } else HttpResponse.resultSuccess(sysPermissionService!!.selectOne(sysPermission) != null)
    }


}
