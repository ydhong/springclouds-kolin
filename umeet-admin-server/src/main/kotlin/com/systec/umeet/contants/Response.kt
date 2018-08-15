package com.systec.umeet.contants
import org.apache.commons.lang.StringUtils
import java.util.*
object  Response {
    val SUCCESS_MSG = "数据加载成功"

    /**
     * 生成json返回结果
     */
    fun resultSuccess(code: Int?, msg: String, data: Any?): Any {
        val rs = HashMap<String, Any>()
        rs.put("code", code!!)
        rs.put("msg", if (StringUtils.isNotEmpty(msg)) msg else SUCCESS_MSG)
        rs.put("data", data ?: Any())
        return rs
    }

    fun resultError(code: Int?, message: String, data: Any?): Any {
        val rs = HashMap<String, Any>()
        rs.put("code", code!!)
        rs.put("data", data ?: Any())
        rs.put("message", if (StringUtils.isNotEmpty(message)) message else "")
        return rs
    }

    /**
     * 生成json返回结果
     */
    fun resultList(code: Int?, msg: String, pageNo: Int?, pageSize: Int?, data: Any?): Any {
        val rs = HashMap<String, Any>()
        rs.put("code", code!!)
        rs.put("msg", if (StringUtils.isNotEmpty(msg)) msg else SUCCESS_MSG)
        rs.put("data", data ?: Any())
        return rs
    }

    /**
     * 生成json返回结果
     */
    fun resultPage(code: Int?, msg: String, pageNo: Int?, pageSize: Int?, data: Any?): Any {
        val rs = HashMap<String, Any>()
        rs.put("code", code!!)
        rs.put("msg", if (StringUtils.isNotEmpty(msg)) msg else SUCCESS_MSG)
        rs.put("data", data ?: Any())
        rs.put("pageNo", pageNo ?: 0)
        rs.put("pageSize", pageSize ?: 10)
        return rs
    }
}


