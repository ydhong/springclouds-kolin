package com.umeet.conference.web

import com.umeet.conference.contants.Constant
import com.umeet.conference.model.User
import com.umeet.conference.service.GlobalConfigurService
import com.umeet.conference.service.UserService
import com.umeet.conference.util.FastJsonUtils
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.List
import java.util.Map
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import com.umeet.conference.contants.CommonException



@RestController
@RequestMapping("/umeet/user")
@Api(value = "UsersController", description = "系统用户接口")
class UserController {

    @Autowired
    var userService :UserService? = null

    @Autowired
    var globalConfigurService : GlobalConfigurService? = null


    @ApiOperation(value = "查询" , httpMethod = "GET")
    @RequestMapping("query", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun query(@RequestBody record: User ) :String {
        val userPage = userService!!.query(record)
        return FastJsonUtils.resultSuccess(200, "成功", userPage)
    }


    @ApiOperation(value = "查询" , httpMethod = "GET")
    @RequestMapping("queryUserBySessionId", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun queryUserBySessionId(request: HttpServletRequest) :String {
        val session: HttpSession = request.getSession();
        //检查账号有效性
        var sessionUser = session.getAttribute(Constant.LOGIN_ADMIN_USER) as User?;
        return FastJsonUtils.resultSuccess(200, "成功", sessionUser)
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存", httpMethod="POST")
    @PostMapping(value = "add", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun save(@RequestBody(required = false) record: User) :String {
       /* if(StringUtils.isNotBlank(record.password)){
            record.password= DigestUtils.md5Hex(record.password);
        }*/
        var type = globalConfigurService!!.getUserType()
        if (record.account != null && "" != record.account) {
            val userList = userService!!.queryOrgUserByAccount(record.account!!,type)
            if (userList.size > 0){
                return FastJsonUtils.resultError(201, "重复账号", null)
            }
        }
        record.sort = (userService!!.queryUserSrotMax() + 1)
        record.roleId = "4"
        record.type = type
        record.isEnable = 1
        val row = userService!!.save(record);
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
    fun update(@RequestBody(required = false) record: User) :String {
        /*if(StringUtils.isNotBlank(record.password)){
            record.password= DigestUtils.md5Hex(record.password);
        }*/
        var type = globalConfigurService!!.getUserType()
        record.type = type
        val row = userService!!.update(record);
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
        val row = userService!!.deleteByPrimaryKey(id);
        if(row == 0) {
            return FastJsonUtils.resultError(202, "删除失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "删除成功", null);
    }

    /**
     * 删除
     */
    @ApiOperation(value = "根据ids批量删除")
    @PostMapping(value = "delete", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun deletes(@RequestBody  params: Map<String, Object>): String {
        val ids = params.get("ids") as List<String>;
        if (CollectionUtils.isEmpty(ids)) {
            return FastJsonUtils.resultError(-200, "操作失败", null);
        }
        try {
            for (id in ids) {
                val record: User =  User();
                record.id=id
                record.isEnable= 0
                userService!!.updateByPrimaryKeySelective(record);
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
                val record: User =  User();
                record.id=id
                record.isEnable= isEnable
                userService!!.updateByPrimaryKeySelective(record);
            }
        } catch (e: Exception) {
            return FastJsonUtils.resultError(-200, "保存失败", null);
        }
        return FastJsonUtils.resultSuccess(200, "成功", null);
    }




    /**
     * 保存
     */
    @ApiOperation(value = "保存", httpMethod="POST")
    @PostMapping(value = "synchronize", produces = arrayOf("application/json;charset=UTF-8"))
    @ResponseBody
    fun synchronize() :String {

        try {
            userService!!.synchronizeADUsers();

        } catch (e: Exception) {
            e.printStackTrace()
            return FastJsonUtils.resultError(-200, "保存失败", null);
        }

        return FastJsonUtils.resultSuccess(200, "成功", null);
    }

}
