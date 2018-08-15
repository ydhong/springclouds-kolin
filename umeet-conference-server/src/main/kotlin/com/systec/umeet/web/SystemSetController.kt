package com.umeet.conference.web
import com.umeet.conference.model.GlobalConfigur
import com.umeet.conference.service.GlobalConfigurService
import com.umeet.conference.util.FastJsonUtils
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.apache.commons.collections.CollectionUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*
import java.util.List
import java.util.Map
import javax.naming.*


@RestController
@RequestMapping("/umeet/systemSet")
@Api(value = "SystemSetController", description = "系统设置接口")
class SystemSetController {

    @Autowired
    var globalConfigurService : GlobalConfigurService? = null

    var logger = LoggerFactory.getLogger("SystemSetController.class")

    @ApiOperation(value = "查询" , httpMethod = "GET")
    @RequestMapping("query", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun query(record: GlobalConfigur) :String {
        val organizations = globalConfigurService!!.select(record)
        return FastJsonUtils.resultSuccess(200, "成功", organizations)
    }

    @ApiOperation(value = "查询" , httpMethod = "GET")
    @RequestMapping("getADSet", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun getADSet(record: GlobalConfigur) :String {
        val adMap = HashMap<String, String>()
        try {
            val map = globalConfigurService!!.readData()
            val isEnableAD = if (map["isEnableAD"] == null) "0" else map["isEnableAD"].toString()
            val isADGroup = if (map["isADGroup"] == null) "0" else map["isADGroup"].toString()
            val adLDAP_URL = if (map["adLDAP_URL"] == null) "" else map["adLDAP_URL"].toString()
            val adDomain_name = if (map["adDomain_name"] == null) "" else map["adDomain_name"].toString()
            val adsearchBase = if (map["adsearchBase"] == null) "" else map["adsearchBase"].toString()
            val adUsername = if (map["adUsername"] == null) "" else map["adUsername"].toString()
            val adPassword = if (map["adPassword"] == null) "" else map["adPassword"].toString()
            val adInterval = if (map["adInterval"] == null) "0" else map["adInterval"].toString()
            adMap["isADGroup"] = isADGroup
            adMap["isEnableAD"] = isEnableAD
            adMap["adInterval"] = adInterval
            adMap["adLDAP_URL"] = adLDAP_URL
            adMap["adsearchBase"] = adsearchBase
            adMap["adUsername"] = adUsername
            adMap["adPassword"] = adPassword
            adMap["adDomain_name"] = adDomain_name

        } catch (e: Exception) {
            return FastJsonUtils.resultFeatures(200, "获取失败", "")
        }

        return FastJsonUtils.resultSuccess(200, "成功", adMap)
    }


    /**
     * @api {post} /systemSet/setADSet 更新AD域配置
     * @apiName setADSet
     * @apiGroup SystemSet Controller
     *
     * @apiParam {String} isEnableAD
     * @apiParam {String} adPassword
     * @apiParam {String} adUsername
     * @apiParam {String} isADGroup
     *
     * @apiSuccess {json} [data='""']
     * @apiSuccessExample {json} Response 200 Example
     * {"message":"OK","status":200}
     */
    @ApiOperation(value = "保存", httpMethod="POST")
    @PostMapping(value = "setADSet", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun setADSet(@RequestBody  map: Map<String, Object>): Any {
        try {
            globalConfigurService!!.setADSet(map)
            return FastJsonUtils.resultSuccess(200,"成功","")
        } catch (e: ConfigurationException) {
            logger.error("AD域参数配置异常,异常:{}", e)
            return FastJsonUtils.resultFeatures(11500, "配置异常，请检查AD域参数配置是否正确。","")
        } catch (e: CommunicationException) {
            logger.error("AD域URL配置异常,异常:{}", e)
            return FastJsonUtils.resultFeatures(11500, "连接异常，请检查AD域URL配置是否正确。","")
        } catch (e: NameNotFoundException) {
            logger.error("AD域参数配置异常,异常:{}", e)
            return FastJsonUtils.resultFeatures(11500, "字符不能被找到，请检查AD域参数配置是否正确。","")
        } catch (e: NumberFormatException) {
            logger.error("AD域参数配置异常,异常:{}", e)
            return FastJsonUtils.resultFeatures(11500, "字符类型转换失败,请检查AD域参数配置是否正确。","")
        } catch (e: InvalidNameException) {
            logger.error("AD域参数配置异常,异常:{}", e)
            return FastJsonUtils.resultFeatures(11500, "无效的字符,请检查AD域参数配置是否正确。","")
        } catch (e: DataIntegrityViolationException) {
            logger.error("AD域参数配置异常,异常:{}", e)
            return FastJsonUtils.resultFeatures(11500, "数据异常,请规范AD域数据或选择正确的AD域节点。","")
        }  catch (e: NamingException) {
            logger.error("AD域参数配置异常,异常:{}", e)
            return FastJsonUtils.resultFeatures(11500, "配置异常，请检查AD域参数配置是否正确。","")
        } catch (e: Exception) {
            logger.error("AD域同步失败,异常:{}", e)
            return FastJsonUtils.resultFeatures(11500, "同步失败！","")
        }

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
                globalConfigurService!!.deleteByPrimaryKey(id);
            }
        } catch (e: Exception) {
            return FastJsonUtils.resultError(-200, "操作失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "操作成功", null);
    }

}
