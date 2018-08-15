package com.systec.umeet.web

import com.systec.umeet.contants.HttpResponse
import com.systec.umeet.contants.ResourcesMapping
import com.systec.umeet.contants.SysConstant
import com.systec.umeet.model.SysMenu
import com.systec.umeet.service.SysMenuService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Api(value = "菜单管理", tags = arrayOf("菜单管理接口"))
@RestController
@RequestMapping(value = "api/sysMenu")
class SysMenuController {

    @Autowired
    var sysMenuService : SysMenuService? = null;

    @ApiOperation(value = "菜单添加", notes = "根据SysMenu添加菜单")
    @ResourcesMapping(element = "添加", code = "sys_menu_add")
    @PostMapping
    fun add(@Validated @RequestBody sysMenu: SysMenu, bindingResult: BindingResult): HttpResponse<*> {
        if (bindingResult.hasErrors()) {
            return HttpResponse.errorParams()
        }
        sysMenuService!!.insert(sysMenu)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "菜单删除", notes = "根据菜单id删除菜单信息")
    @ResourcesMapping(element = "删除", code = "sys_menu_delete")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): HttpResponse<*> {
        sysMenuService!!.deleteByPrimaryKey(id)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "菜单查询", notes = "可分页并可根据菜单名称模糊查询")
    @ResourcesMapping(element = "查询", code = "sys_menu_query")
    @GetMapping
    fun query(requestDto: SysMenu): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysMenuService!!.getPage(requestDto))
    }

    @ApiOperation(value = "菜单修改", notes = "根据传递的SysMenu对象来更新, SysMenu对象必须包含id")
    @ResourcesMapping(element = "修改", code = "sys_menu_update")
    @PutMapping
    fun update(@Validated @RequestBody sysMenu: SysMenu, bindingResult: BindingResult): HttpResponse<*> {
        if (bindingResult.hasErrors()) {
            return HttpResponse.errorParams()
        }
        sysMenuService!!.update(sysMenu)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "菜单详情查询", notes = "根据id查询菜单详细信息")
    @ResourcesMapping(element = "修改", code = "sys_menu_update")
    @GetMapping("/{id}")
    fun info(@PathVariable id: Int): HttpResponse<*> {
        sysMenuService!!.selectByPrimaryKey(id)
        return HttpResponse.resultSuccess()
    }

    @ApiOperation(value = "菜单树查询", notes = "查询所有可用菜单并返回树状结构")
    @ResourcesMapping(element = "菜单树查询", code = "sys_menu_tree")
    @GetMapping("/tree")
    fun tree(): HttpResponse<*> {
        return HttpResponse.resultSuccess(sysMenuService!!.loadAllMenuTree())
    }

    @ApiOperation(value = "菜单是否已存在", notes = "根据SysMenu对象设定的字段值来查询判断")
    @GetMapping(SysConstant.API_NO_PERMISSION + "/exist")
    fun exist(sysMenu: SysMenu?): HttpResponse<*> {
        return if (sysMenu == null) {
            HttpResponse.errorParams()
        } else HttpResponse.resultSuccess(sysMenuService!!.selectOne(sysMenu) != null)
    }

}
