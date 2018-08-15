package com.umeet.conference.web

import com.umeet.conference.model.Organization
import com.umeet.conference.service.GlobalConfigurService
import com.umeet.conference.service.OrganizationService
import com.umeet.conference.util.FastJsonUtils
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.apache.commons.collections.CollectionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.List
import java.util.Map

@RestController
@RequestMapping("/umeet/org")
@Api(value = "OrganizationController", description = "系统机构接口")
class OrganizationController {

    @Autowired
    var organizationService : OrganizationService? = null

    @Autowired
    var globalConfigurService :GlobalConfigurService? = null

    @ApiOperation(value = "查询" , httpMethod = "GET")
    @RequestMapping("query", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun query(record: Organization) :String {
        var type = globalConfigurService!!.getUserType()
        record.type = type
        val organizations = organizationService!!.select(record)
        return FastJsonUtils.resultSuccess(200, "成功", organizations)
    }

    @ApiOperation(value = "查询" , httpMethod = "GET")
    @RequestMapping("queryOrgByOrgId", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun queryOrgByOrgId(record: Organization) :String {
        val organizations = organizationService!!.select(record)
        return FastJsonUtils.resultSuccess(200, "成功", organizations)
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存", httpMethod="POST")
    @PostMapping(value = "add", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun save(@RequestBody(required = false) record: Organization) :String {
        var type = globalConfigurService!!.getUserType()
        record.type = type
        if("0".equals(record.pid)){
            record.levels = record.name
        }else{
            var parent = organizationService!!.selectByPrimaryKey(record.pid!!)
            record.levels = parent.levels + "," + record.name
        }
        val row = organizationService!!.save(record);
        if(row == 0) {
            return FastJsonUtils.resultError(201, "保存失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "成功", null);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "修改", httpMethod="POST")
    @PostMapping(value = "update", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun update(@RequestBody(required = false) record: Organization) :String {
        var type = globalConfigurService!!.getUserType()
        record.type = type
        val row = organizationService!!.update(record);
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
        val row = organizationService!!.deleteByPrimaryKey(id);
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
                organizationService!!.deleteByPrimaryKey(id);
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
                val record: Organization =  Organization();
                record.id=id
                organizationService!!.updateByPrimaryKeySelective(record);
            }
        } catch (e: Exception) {
            return FastJsonUtils.resultError(-200, "保存失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "成功", null);
    }
}
