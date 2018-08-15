package com.umeet.conference.service
import com.github.pagehelper.PageInfo
import com.umeet.conference.base.BaseServiceImpl
import com.umeet.conference.dao.GlobalConfigurDao
import com.umeet.conference.model.GlobalConfigur
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tk.mybatis.mapper.common.Mapper
import java.util.*
import java.util.Map
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler




@Service
class GlobalConfigurService : BaseServiceImpl<GlobalConfigur> (){

    @Autowired
    var globalConfigurDao : GlobalConfigurDao? = null

    @Autowired
    var adDataService : ADDataService ?= null


    override fun getMapper(): Mapper<GlobalConfigur> {
        return globalConfigurDao!!;
    }

    /**
     * 根据条件分页查询用户
     */
    fun getPage(record: GlobalConfigur): PageInfo<GlobalConfigur> {
        var orderSqlStr = " sort "
        return super.selectPage(record.page , record.rows ,record,orderSqlStr);
    }

    fun readData(): HashMap<String, String> {
        val list = super.select(GlobalConfigur());
        val map = HashMap<String, String>()
        for (g in list) {
            map.put(g.name!!, g.value!!)
        }
        return map

    }

    fun setADSet(map: Map<String, Object>) {
        val isEnableAD = if (map["isEnableAD"] == null) "" else map["isEnableAD"].toString()
        val passWord = if (map["adPassword"] == null) "" else map["adPassword"].toString()
        val userName = if (map["adUsername"] == null) "" else map["adUsername"].toString()
        val isADGroup = if (map["isADGroup"] == null) "" else map["isADGroup"].toString()
        val timer = Timer()
        timer.cancel()
        writeData(map)
        if ("1" == isEnableAD) {
            if ("1" == isADGroup) {
                adDataService!!.syncOffice(userName, passWord) //同步组织机构
                adDataService!!.syncUsers(userName, passWord) //同步组织机构用户
            }
            /*if ("0" == isADGroup) {
                adDataService!!.syncGroupUsers(passWord, userName)
            }*/
        } else if ("0" == isEnableAD) {
            //启用自定义用户 禁用其他用户
            val mRunnable = Runnable {
                run {
                    try {
                        adDataService!!.enableUser("3")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            Thread(mRunnable).start()
        }

    }

    fun writeData(map: Map<String, Object>) {
        for(entry in map.entrySet()){
            if(entry.key == null || "".equals(entry.key)) continue
            var configur = GlobalConfigur(entry.key, entry.value.toString())
            super.update(configur)
        }
    }

    fun getUserType(): String {
        var type = "1"
        var map = readData();
        if(map["isEnableAD"].toString().equals("0") ){
            type = "2"
        }else if(map["isEnableAD"].toString().equals("1") ){
            if( "1".equals(map["isADGroup"].toString())) type = "1" else type = "3"
        }
        return type
    }
}
