package com.systec.umeet.web
import com.systec.umeet.contants.*
import com.systec.umeet.model.MenuTree
import com.systec.umeet.model.SysUser
import com.systec.umeet.model.SysUserRolePk
import com.systec.umeet.service.SysMenuService
import com.systec.umeet.service.SysUserRolePkService
import com.systec.umeet.service.SysUserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * @description:
 * @author: super.wu
 * @date: Created in 2018/5/2 0002
 * @modified By:
 * @version: 1.0
 */
@Api(value = "用户管理", tags = arrayOf("用户管理接口"))
@RestController
@RequestMapping(value = "api/sysUser")
class SysUserController {

    @Autowired
    private val sysUserService: SysUserService? = null

    @Autowired
    private val sysUserRolePkService: SysUserRolePkService? = null

    @Autowired
    private val sysMenuService: SysMenuService? = null

    @ApiOperation(value = "用户添加", notes = "根据UserDto保存用户对象")
    @ResourcesMapping(element = "添加", code = "sys_user_add")
    @PostMapping
    fun add(@Validated @RequestBody dto: SysUser, bindingResult: BindingResult): HttpResponse<*> {
        if (bindingResult.hasErrors() || StringUtils.isBlank(dto.password)) {
            return HttpResponse.errorParams()
        }
        sysUserService!!.save(dto)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "角色删除", notes = "根据角色id删除角色信息")
    @ResourcesMapping(element = "删除", code = "sys_user_delete")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int?): HttpResponse<*> {
        sysUserService!!.deleteByPrimaryKey(id!!)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "用户查询", notes = "可分页并可根据用户名称模糊检索")
    @ResourcesMapping(element = "查询", code = "sys_user_query")
    @GetMapping
    fun query(record: SysUser): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysUserService!!.getPage(record))
    }

    @ApiOperation(value = "用户修改", notes = "根据传递的SysUser对象来更新, SysUser对象必须包含id")
    @ResourcesMapping(element = "修改", code = "sys_user_update")
    @PutMapping
    fun update(@Validated @RequestBody dto: SysUser, bindingResult: BindingResult): HttpResponse<*> {
        if (bindingResult.hasErrors() || dto.id == null) {
            return HttpResponse.errorParams()
        }
        sysUserService!!.update(dto)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "用户详情查询", notes = "根据id查询用户详细信息")
    @ResourcesMapping(element = "查询详情", code = "sys_user_detail")
    @GetMapping("/{id}")
    fun info(@PathVariable id: Int): HttpResponse<*> {
        val sysUser = SysUser()
        sysUser.id = id
        return HttpResponse.resultSuccess(sysUserService!!.selectOne(sysUser))
    }

    @ApiOperation(value = "用户编辑信息查询", notes = "根据id查询用户修改信息")
    @ResourcesMapping(element = "查询编辑详情", code = "sys_user_edit_info")
    @GetMapping("/edit/{id}")
    fun editInfo(@PathVariable id: Int): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysUserService!!.selectUserGroupInfo(id))
    }

    @ApiOperation(value = "用户关联角色批量添加", notes = "保存多个SysUserRolePk对象")
    @ResourcesMapping(element = "添加", code = "sys_user_role_add")
    @PostMapping("/batchRoleAdd")
    fun batchRoleAdd(@RequestBody list: kotlin.collections.List<SysUserRolePk>?): HttpResponse<*> {
        if (list == null || list.size == 0) {
            return HttpResponse.errorParams()
        }
        sysUserRolePkService!!.batchInsert(list)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "用户关联角色批量删除", notes = "根据用户id和多个角色id删除关联")
    @ResourcesMapping(element = "删除", code = "sys_user_role_delete")
    @PostMapping("/batchRoleDelete")
    fun batchRoleDelete(@Validated @RequestBody dto: HashMap<String,Any>): HttpResponse<*> {
        if (dto["userId"] == null || dto["roleIds"] == null) {
            return HttpResponse.errorParams()
        }
        sysUserRolePkService!!.batchDelete(dto)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "用户已关联角色查询", notes = "根据用户id查询该用户已关联的角色并返回角色列表")
    @ResourcesMapping(element = "查询", code = "sys_user_role")
    @GetMapping("/userRoleList")
    fun userRoleList(dto: SysUserRolePk): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysUserRolePkService!!.loadUserRoles(dto))
    }

    @ApiOperation(value = "用户未关联角色查询", notes = "根据用户id查询该用户未关联的角色并返回角色列表")
    @ResourcesMapping(element = "查询", code = "sys_user_no_role")
    @GetMapping("/userNoRelationRoleList")
    fun userNoRelationRoleList(dto: SysUserRolePk): Any {
        return HttpResponse.resultSuccess(sysUserRolePkService!!.loadUserNoRelationRoles(dto))
    }


    /**
     * 下面是不要校验权限的接口，注意请加上常量声明 Start--->
     */

    @ApiOperation(value = "查询用户名是否存在", notes = "根据用户Id查询分配的角色权限下面的资源列表")
    @GetMapping(SysConstant.API_NO_PERMISSION + "checkUserNameIsExist")
    fun checkUserNameIsExist(@RequestParam username: String): HttpResponse<*> {
        val sysUser = SysUser()
        sysUser.username = username
        return HttpResponse.resultSuccess(sysUserService!!.selectOne(sysUser) != null)
    }

    @ApiOperation(value = "获取登陆授权后的用户信息", notes = "根据授权Authentication中UserEntity中的userId获取")
    @GetMapping(SysConstant.API_NO_PERMISSION + "userInfo")
    fun userInfo(authentication: Authentication): HttpResponse<*> {
        val userEntity = authentication.principal as UserEntity
        val user = SysUser()
        user.id = userEntity.userId
        val result = sysUserService!!.selectOne(user)
        return HttpResponse.resultSuccess(result)
    }

    @ApiOperation(value = "用户可用菜单树查询", notes = "根据用户权限查询已分配好的菜单")
    @GetMapping(SysConstant.API_NO_PERMISSION + "userMenuTree")
    fun userMenuTree(authentication: Authentication): HttpResponse<*> {
        val userEntity = authentication.principal as UserEntity

        var result: kotlin.collections.List<MenuTree> = ArrayList<MenuTree>()
        if (userEntity.isAdmin) {
            result = sysMenuService!!.loadAllMenuTree()
        } else if (userEntity.isRoles) {
            result = sysUserService!!.selectUserMenuTree(userEntity.userId)
        }
        return HttpResponse.resultSuccess(result)
    }

    @ApiOperation(value = "用户所有资源编码查询", notes = "根据用户id查询该用户已关联的角色并返回角色列表")
    @GetMapping(SysConstant.API_NO_PERMISSION + "userAllResourceCodes")
    fun userAllResourceCodes(authentication: Authentication): HttpResponse<*> {
        val userEntity = authentication.principal as UserEntity
        return HttpResponse.resultSuccess(sysUserService!!.selectUserAllResourceCodes(userEntity.userId))
    }

    @ApiOperation(value = "用户多个资源编码匹配", notes = "根据用户id查询该用户已关联的角色并返回角色列表")
    @GetMapping(SysConstant.API_NO_PERMISSION + "userResourceCodes")
    fun userResourceCodes(authentication: Authentication, resourceCodes: Array<String>): HttpResponse<*> {
        if (ArrayUtils.isEmpty(resourceCodes)) {
            return HttpResponse.errorParams()
        }
        val userEntity = authentication.principal as UserEntity
        // 如果是超级管理员则直接返回资源
        if (userEntity.isAdmin) {
            return HttpResponse.resultSuccess(resourceCodes)
        }
        val map = object : HashMap<String, Any>(2) {
            init {
                put("userId", userEntity.userId)
                put("codes", resourceCodes)
            }
        }
        return HttpResponse.resultSuccess(sysUserService!!.selectUserResourceCodes(map))
    }

    /**
     * 上面是不要校验权限的接口，注意请加上常量声明 <---End
     */
}
