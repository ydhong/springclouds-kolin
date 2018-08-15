package com.systec.umeet.web

import com.systec.umeet.contants.HttpResponse
import com.systec.umeet.contants.ResourcesMapping
import com.systec.umeet.contants.SysConstant
import com.systec.umeet.model.SysUserGroup
import com.systec.umeet.model.SysUserGroupPk
import com.systec.umeet.model.SysUserGroupRolePk
import com.systec.umeet.service.SysUserGroupPkService
import com.systec.umeet.service.SysUserGroupRolePkService
import com.systec.umeet.service.SysUserGroupService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@Api(value = "用户组管理", tags = arrayOf("用户组管理接口"))
@RestController
@RequestMapping(value = "api/sysUserGroup")
class SysUserGroupController {

    @Autowired
    var sysUserGroupService : SysUserGroupService? = null;

    @Autowired
    val sysUserGroupPkService: SysUserGroupPkService? = null

    @Autowired
    val sysUserGroupRolePkService: SysUserGroupRolePkService? = null

    @ApiOperation(value = "用户组添加", notes = "根据SysUserGroup对象创建用户组")
    @ResourcesMapping(element = "添加", code = "sys_user_group_add")
    @PostMapping
    fun save(@RequestBody sysUserGroup: SysUserGroup, bindingResult: BindingResult): HttpResponse<*> {
        if (bindingResult.hasErrors()) {
            return HttpResponse.errorParams()
        }
        sysUserGroupService!!.insert(sysUserGroup)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "用户组删除", notes = "根据用户组id删除用户组信息")
    @ResourcesMapping(element = "删除", code = "sys_user_group_delete")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): HttpResponse<*> {
        sysUserGroupService!!.deleteById(id)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "用户组查询", notes = "可分页并可根据用户组名称模糊检索")
    @ResourcesMapping(element = "查询", code = "sys_user_group_query")
    @GetMapping
    fun query(requestDto: SysUserGroup): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysUserGroupService!!.getPage(requestDto))
    }

    @ApiOperation(value = "用户组修改", notes = "根据传递的SysUserGroup对象来更新, SysUserGroup对象必须包含id")
    @ResourcesMapping(element = "修改", code = "sys_user_group_update")
    @PutMapping
    fun update(@Validated @RequestBody dto: SysUserGroup, bindingResult: BindingResult): HttpResponse<*> {
        if (bindingResult.hasErrors()) {
            return HttpResponse.errorParams()
        }
        sysUserGroupService!!.update(dto)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "用户组详细信息查询", notes = "根据id查询用户组修改信息")
    @ResourcesMapping(element = "查询详情", code = "sys_user_group_info")
    @GetMapping("/{id}")
    fun info(@PathVariable id: Int): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysUserGroupService!!.selectByPrimaryKey(id))
    }

    @ApiOperation(value = "用户组关联用户批量添加", notes = "添加多个SysUserGroupPk对象")
    @ResourcesMapping(element = "批量添加", code = "sys_group_user_add")
    @PostMapping("/batchUserAdd")
    fun batchUserAdd(@RequestBody list: kotlin.collections.List<SysUserGroupPk>?): HttpResponse<*> {
        if (list == null || list.size == 0) {
            return HttpResponse.errorParams()
        }
        sysUserGroupPkService!!.batchInsert(list)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "用户组关联用户批量删除", notes = "根据用户id和多个角色id删除关联")
    @ResourcesMapping(element = "批量删除", code = "sys_group_user_delete")
    @PostMapping("/batchUserDelete")
    fun batchUserDelete(@Validated @RequestBody dto: HashMap<String,Any>): HttpResponse<*> {
        if (dto["userId"] == null || dto["userIds"] == null) {
            return HttpResponse.errorParams()
        }
        sysUserGroupPkService!!.batchDelete(dto["groupId"] as Int, dto["userIds"] as Array<Long>)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "用户组未关联用户查询", notes = "根据用户id查询该用户未关联的角色并返回角色列表")
    @ResourcesMapping(element = "查询", code = "sys_group_no_user")
    @GetMapping("/groupNoRelationUserList")
    fun groupNoRelationUserList(dto: SysUserGroupPk): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysUserGroupPkService!!.loadGroupNoRelationUsers(dto))
    }

    @ApiOperation(value = "用户组已关联用户查询", notes = "根据用户id查询该用户已关联的角色并返回角色列表")
    @ResourcesMapping(element = "查询", code = "sys_group_user")
    @GetMapping("/groupUserList")
    fun groupUserList(dto: SysUserGroupPk): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysUserGroupPkService!!.loadGroupUsers(dto))
    }

    @ApiOperation(value = "用户组关联角色批量添加", notes = "保存多个SysRolePermissionPk对象")
    @ResourcesMapping(element = "批量添加", code = "sys_group_role_add")
    @PostMapping("/batchRoleAdd")
    fun batchRoleAdd(@RequestBody list: kotlin.collections.List<SysUserGroupRolePk>?): HttpResponse<*> {
        if (list == null || list.size == 0) {
            return HttpResponse.errorParams()
        }
        sysUserGroupRolePkService!!.batchInsert(list)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "用户组关联角色批量删除", notes = "根据角色id和多个权限id删除关联")
    @ResourcesMapping(element = "批量删除", code = "sys_group_role_delete")
    @PostMapping("/batchRoleDelete")
    fun batchRoleDelete(@Validated @RequestBody dto: HashMap<String,Any>): HttpResponse<*> {

        if (dto["roleId"] == null || dto["roleIds"] == null) {
            return HttpResponse.errorParams()
        }
        sysUserGroupRolePkService!!.batchDelete(dto["groupId"] as Int, dto["roleIds"] as IntArray)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "用户组未关联角色查询", notes = "根据角色id查询该角色未关联的权限并返回权限列表")
    @ResourcesMapping(element = "查询", code = "sys_group_no_role")
    @GetMapping("/groupNoRelationRoleList")
    fun groupNoRelationRoleList(dto: SysUserGroupRolePk): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysUserGroupRolePkService!!.loadGroupNoRelationRoles(dto))
    }

    @ApiOperation(value = "用户组已关联角色查询", notes = "根据角色id查询该角色已关联的权限并返回权限列表")
    @ResourcesMapping(element = "查询", code = "sys_group_role")
    @GetMapping("/groupRoleList")
    fun groupRoleList(dto: SysUserGroupRolePk): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysUserGroupRolePkService!!.loadGroupRoles(dto))
    }

    @ApiOperation(value = "用户所有可用资源查询", notes = "根据用户Id查询分配的角色权限下面的资源列表")
    @GetMapping(SysConstant.API_NO_PERMISSION + "exist")
    fun exist(@RequestParam name: String): HttpResponse<*> {
        val sysUserGroup = SysUserGroup()
        sysUserGroup.name = name
        return HttpResponse.resultSuccess(sysUserGroupService!!.selectOne(sysUserGroup) != null)
    }
}
