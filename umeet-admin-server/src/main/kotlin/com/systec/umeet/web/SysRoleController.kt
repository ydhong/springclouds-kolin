package com.systec.umeet.web
import com.systec.umeet.contants.HttpResponse
import com.systec.umeet.contants.ResourcesMapping
import com.systec.umeet.contants.SysConstant
import com.systec.umeet.model.SysRole
import com.systec.umeet.model.SysRolePermissionPk
import com.systec.umeet.service.SysRolePermissionPkService
import com.systec.umeet.service.SysRoleService
import com.systec.umeet.utills.ListUtils
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@Api(value = "角色管理", tags = arrayOf("角色管理接口"))
@RestController
@RequestMapping(value = "api/sysRole")
class SysRoleController {

    @Autowired
    var sysRoleService : SysRoleService? = null;

    @Autowired
    var sysRolePermissionPkService: SysRolePermissionPkService? = null

    @ApiOperation(value = "角色添加", notes = "根据SysRole对象创建角色")
    @ResourcesMapping(element = "添加", code = "sys_role_add")
    @PostMapping
    fun add(@Validated @RequestBody role: SysRole, bindingResult: BindingResult): HttpResponse<*> {
        if (bindingResult.hasErrors()) {
            return HttpResponse.errorParams()
        }
        sysRoleService!!.insert(role)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "角色删除", notes = "根据角色id删除角色信息")
    @ResourcesMapping(element = "删除", code = "sys_role_delete")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): HttpResponse<*> {
        sysRoleService!!.deleteById(id)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "角色查询", notes = "可分页并可根据角色名称模糊检索")
    @ResourcesMapping(element = "查询", code = "sys_role_query")
    @GetMapping
    fun query(requestDto: SysRole): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysRoleService!!.getPage(requestDto))
    }

    @ApiOperation(value = "角色修改", notes = "根据传递的SysRole对象来更新, SysRole对象必须包含id")
    @ResourcesMapping(element = "修改", code = "sys_role_update")
    @PutMapping
    fun update(@Validated @RequestBody sysRole: SysRole, bindingResult: BindingResult): HttpResponse<*> {
        if (bindingResult.hasErrors()) {
            return HttpResponse.errorParams()
        }
        sysRoleService!!.update(sysRole)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "用户详情查询", notes = "根据id查询用户详细信息")
    @ResourcesMapping(element = "查询详情", code = "sys_role_detail")
    @GetMapping("/{id}")
    fun info(@PathVariable id: Int): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysRoleService!!.selectByPrimaryKey(id))
    }

    @ApiOperation(value = "角色关联权限批量保存", notes = "保存多个SysRolePermissionPk对象")
    @ResourcesMapping(element = "添加", code = "sys_role_permission_add")
    @PostMapping("/batchPermissionAdd")
    fun batchPermissionAdd(@RequestBody list: kotlin.collections.List<SysRolePermissionPk>): HttpResponse<*> {
        if (ListUtils.isEmpty(list)) {
            return HttpResponse.errorParams()
        }
        sysRolePermissionPkService!!.batchInsert(list)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "角色关联权限批量删除", notes = "根据角色id和多个权限id删除关联")
    @ResourcesMapping(element = "删除", code = "sys_role_permission_delete")
    @PostMapping("/batchPermissionDelete")
    fun batchPermissionDelete(@Validated @RequestBody dto: HashMap<String,Any>): HttpResponse<*> {
        if (dto["permissionIds"] != null) {
            return HttpResponse.errorParams()
        }
        sysRolePermissionPkService!!.batchDelete(dto["roleId"] as Int?, dto["permissionIds"] as Array<Int>)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "角色未关联权限查询", notes = "根据角色id查询该角色未关联的权限并返回权限列表")
    @ResourcesMapping(element = "查询", code = "sys_role_no_permission")
    @GetMapping("/roleNoRelationPermissionList")
    fun roleNoRelationPermissionList(dto: SysRolePermissionPk): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysRolePermissionPkService!!.loadRoleNoRelationPermissions(dto))
    }

    @ApiOperation(value = "角色已关联权限查询", notes = "根据角色id查询该角色已关联的权限并返回权限列表")
    @ResourcesMapping(element = "查询", code = "sys_role_permission")
    @GetMapping("/rolePermissionList")
    fun rolePermissionList(dto: SysRolePermissionPk): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysRolePermissionPkService!!.loadRolePermissions(dto))
    }

    @ApiOperation(value = "角色是否已存在", notes = "根据SysRole对象设定的字段值来查询判断")
    @GetMapping(SysConstant.API_NO_PERMISSION + "exist")
    fun exist(sysRole: SysRole?): HttpResponse<*> {
        return if (sysRole == null) {
            HttpResponse.errorParams()
        } else HttpResponse.resultSuccess(sysRoleService!!.selectOne(sysRole) != null)
    }


}
