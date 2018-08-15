package com.umeet.conference.web

import com.umeet.conference.model.GlobalConfigur
import com.umeet.conference.service.GlobalConfigurService
import com.umeet.conference.util.FastJsonUtils
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.apache.commons.collections.CollectionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.List
import java.util.Map

@RestController
@RequestMapping("/umeet/globalConfigur")
@Api(value = "GlobalConfigurController", description = "系统配置接口")
class GlobalConfigurController {

    @Autowired
    var globalConfigurService : GlobalConfigurService? = null;

    @ApiOperation(value = "查询" , httpMethod = "GET")
    @RequestMapping("query", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun query(record: GlobalConfigur) :String {
        val globalConfigurPage = globalConfigurService!!.getPage(record)
        return FastJsonUtils.resultSuccess(200, "成功", globalConfigurPage)
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存", httpMethod="POST")
    @PostMapping(value = "save", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun save(@RequestBody(required = false) record: GlobalConfigur) :String {
        val row = globalConfigurService!!.save(record);
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
        val row = globalConfigurService!!.deleteByPrimaryKey(id);
        if(row == 0) {
            return FastJsonUtils.resultError(202, "删除失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "删除成功", null);
    }




}
