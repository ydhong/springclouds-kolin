package com.umeet.conference.web

import com.umeet.conference.contants.Constant
import com.umeet.conference.model.Privilege
import com.umeet.conference.model.RoleAndPrivilege
import com.umeet.conference.model.User
import com.umeet.conference.service.PrivilegeService
import com.umeet.conference.util.FastJsonUtils
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.apache.commons.collections.CollectionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.List
import java.util.Map
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/umeet/privilege")
@Api(value = "PrivilegeController", description = "系统角色接口")
class PrivilegeController {

    @Autowired
    var privilegeService : PrivilegeService? = null;

    @ApiOperation(value = "查询" , httpMethod = "GET")
    @RequestMapping("query", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun query(record: Privilege) :String {
        val privilegePage = privilegeService!!.getPage(record)
        return FastJsonUtils.resultSuccess(200, "成功", privilegePage)
    }




    /**
     * 保存
     */
    @ApiOperation(value = "保存", httpMethod="POST")
    @PostMapping(value = "save", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun save(@RequestBody(required = false) record: Privilege) :String {
        val row = privilegeService!!.save(record);
        if(row == 0) {
            return FastJsonUtils.resultError(201, "保存失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "成功", null);
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "delete/{id}", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun delete(@PathVariable id: Int): String {
        val row = privilegeService!!.deleteByPrimaryKey(id);
        if(row == 0) {
            return FastJsonUtils.resultError(202, "删除失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "删除成功", null);
    }

    /**
     * 删除
     */
    @ApiOperation(value = "根据ids批量删除")
    @PostMapping(value = "deletes", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun deletes(@RequestBody  params: Map<String, Object>): String {
        val ids = params.get("ids") as List<Int>;
        if (CollectionUtils.isEmpty(ids)) {
            return FastJsonUtils.resultError(-200, "操作失败", null);
        }
        try {
            for (id in ids) {
                privilegeService!!.deleteByPrimaryKey(id);
            }
        } catch (e: Exception) {
            return FastJsonUtils.resultError(-200, "操作失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "操作成功", null);
    }

    /**
     * 启用
     */
    @ApiOperation(value = "根据ids批量启用或禁用")
    @PostMapping(value = "enables", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun enables(@RequestBody  params: Map<String, Object>): String {
        @SuppressWarnings("unchecked")
        val ids = params.get("ids") as List<String>;
        val isEnable = params.get("isEnable") as Int
        if (CollectionUtils.isEmpty(ids)) {
            return FastJsonUtils.resultError(-200, "操作失败", null);
        }
        try {
            for (id in ids) {
                val record: Privilege =  Privilege();
                record.id=id
                privilegeService!!.updateByPrimaryKeySelective(record);
            }
        } catch (e: Exception) {
            return FastJsonUtils.resultError(-200, "保存失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "成功", null);
    }
}
