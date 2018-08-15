package com.systec.umeet.utills


import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


/**
 * @description: 毫秒转换成年月日时分秒字符串
 * @author: super.wu
 * @date: Created in 2018/6/12 0012
 * @modified By:
 * @version: 1.0
 */
class ToStringDateSerializer : JsonSerializer<Long>() {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(aLong: Long?, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
        jsonGenerator.writeString(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(aLong!!)).toString())
    }

}
