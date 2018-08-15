package com.umeet.conference.service
import com.alibaba.fastjson.JSONObject
import com.umeet.conference.contants.CommonException
import com.umeet.conference.model.Organization
import com.umeet.conference.model.User
import com.umeet.conference.util.NullAwareBeanUtilsBean
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.naming.NamingEnumeration
import java.util.Hashtable
import java.util.ArrayList
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors
import javax.naming.AuthenticationException
import javax.naming.Context
import javax.naming.NamingException
import javax.naming.directory.*
import javax.naming.ldap.*


@Service
class ADDataService {

    var logger = LoggerFactory.getLogger("ADDataService.class")

    @Autowired
    var globalConfigurService:GlobalConfigurService ?= null

    @Autowired
    var organizationService:OrganizationService ?= null

    @Autowired
    var userService:UserService ?= null

    /**
     * 设置AD域的搜索范围、节点等
     * @param searchFilter
     * @param returnedAtts
     * @param userName
     * @param password
     * @return
     * @throws NamingException
     */
    @Throws(NamingException::class)
    fun getAnswers(searchFilter: String, returnedAtts: Array<String>, userName: String, password: String): NamingEnumeration<*>? {
        val HashEnv = getTable(userName, password)
        val ctx = InitialLdapContext(HashEnv, null)
        val searchCtls = SearchControls() //Create the search controls
        searchCtls.searchScope = SearchControls.SUBTREE_SCOPE //Specify the search scope

        val map = globalConfigurService!!.readData()
        val searchBase = if (map.get("adsearchBase") == null) "" else map.get("adsearchBase").toString()
        //		String searchBase = Config.getAdsearchBase(); //Specify the Base for the search//搜索域节点
        searchCtls.returningAttributes = returnedAtts //设置返回属性集
        //Search for objects using the filter

        return ctx.search(searchBase, searchFilter, searchCtls)

    }


    /**
     * 用户登录通过AD域验证
     * @param loginName
     * @param password
     * @return
     */
    fun checkLogin(loginName: String, password: String): Boolean? {
        var isLogin: Boolean? = false
        var ctx: DirContext? = null
        val map = globalConfigurService!!.readData()
        val adDomainName = if (map.get("adDomain_name") == null) "" else map.get("adDomain_name").toString()

        val HashEnv = getTable(loginName + adDomainName, password)
        try {
            ctx = InitialDirContext(HashEnv)// 初始化上下文
            isLogin = true
        } catch (e: AuthenticationException) {
            throw CommonException("身份验证失败", e)
        } catch (e: javax.naming.CommunicationException) {
            throw CommonException("AD域连接失败", e)
        } catch (e: Exception) {
            throw CommonException("身份验证未知异常", e)
        } finally {
            if (null != ctx) {
                try {
                    ctx.close()
                    ctx = null
                } catch (e: Exception) {
                    throw CommonException("身份验证未知异常", e)
                }

            }
        }
        return isLogin
    }


    /**
     * 连接AD域
     * @param userName
     * @param password
     * @return
     */
    fun getTable(userName: String, password: String): Hashtable<String, String> {
        val HashEnv = Hashtable<String, String>()
        val map = globalConfigurService!!.readData()
        val LDAP_URL = if (map.get("adLDAP_URL") == null) "" else map.get("adLDAP_URL").toString()
        // String LDAP_URL = Config.getAdLDAP_URL(); //LDAP访问地址
        HashEnv[Context.SECURITY_AUTHENTICATION] = "simple" //LDAP访问安全级别
        HashEnv[Context.SECURITY_PRINCIPAL] = userName //AD User
        HashEnv[Context.SECURITY_CREDENTIALS] = password //AD Password
        HashEnv[Context.INITIAL_CONTEXT_FACTORY] = "com.sun.jndi.ldap.LdapCtxFactory" //LDAP工厂类
        HashEnv[Context.PROVIDER_URL] = LDAP_URL
        return HashEnv
    }


    /**
     * 从AD域同步机构
     * @return
     * @throws NamingException
     */
    @Throws(Exception::class)
    fun syncOffice(userName: String, passWord: String) {
        val pageSize = 1000
        val searchFilter = "objectClass=organizationalUnit"
        //name 名称  whenChangedAD域字段的更新时间  uSNCreated AD域机构的唯一标识  description 描述
        val returnedAtts = arrayOf("name", "whenChanged", "uSNCreated", "distinguishedName", "street", "postalCode", "description") // 定制返回属性
        val ht = getTable(userName, passWord)
        val ctx:LdapContext = InitialLdapContext(ht, null)
        val searchCtls = SearchControls() //Create the search controls
        searchCtls.searchScope = SearchControls.SUBTREE_SCOPE //Specify the search scope
        val map = globalConfigurService!!.readData()
        val searchBase = if (map.get("adsearchBase") == null) "" else map.get("adsearchBase").toString()
        searchCtls.returningAttributes = returnedAtts //设置返回属性集
        val ctls = arrayOf<Control>(PagedResultsControl(pageSize, false)) // 设置分页读取AD域数据
        ctx.requestControls = ctls
        var cook: ByteArray? = null
        val orgList = ArrayList<JSONObject>()
        do {
            val answer = ctx.search(searchBase, searchFilter, searchCtls)
            if (answer != null && answer != null) {

                while (answer.hasMoreElements()) {
                    val jsonO = JSONObject()
                    val sr = answer.next() as SearchResult
                    val Attrs = sr.attributes
                    if (Attrs != null) {
                        val ne = Attrs.all
                        while (ne.hasMore()) {
                            val Attr = ne.next() as Attribute
                            val AttributeID = Attr.getID().toString()
                            // 读取属性值
                            val values = Attr.getAll()
                            if (values != null) { // 迭代
                                while (values!!.hasMoreElements()) {
                                    val value = values!!.nextElement().toString()
                                    createJsonO(jsonO, AttributeID, value)   //填充jsonO对象
                                }
                            }
                        }
                    }
                    orgList.add(jsonO)
                }
            }
            cook = parseControls(ctx.responseControls)
            ctx.requestControls = arrayOf<Control>(PagedResultsControl(pageSize, cook, Control.CRITICAL))
        } while (cook != null && cook.size != 0)
        ctx.close()
        //设置AD域的搜索范围节点等等
        sortOrg(orgList) //对获取的机构列表排序
        createOrg(orgList) //同步组机构到数据库
        //syncOfficeState(arrayList) //同步AD域数据和系统数据状态（是否禁用）

    }


    /**
     * 填充jsonO对象
     * @param jsonO
     * @param AttributeID
     * @param value
     */
    fun createJsonO(jsonO: JSONObject, AttributeID: String, value: String) {
        if ("name" == AttributeID) {
            jsonO.put("name", value)
        }

        if ("uSNCreated" == AttributeID) {
            jsonO.put("uSNCreated", value)
        }

        if ("whenChanged" == AttributeID) {
            jsonO.put("whenChanged", value)
        }

        if ("street" == AttributeID) {
            jsonO.put("street", value)
        }

        if ("postalCode" == AttributeID) {
            jsonO.put("postalCode", value)
        }

        if ("description" == AttributeID) {
            jsonO.put("description", value)
        }

        if ("distinguishedName" == AttributeID) {
            jsonO.put("distinguishedName", value)

        }
    }


    /**
     * 对读取的机构列表按照levels字段的长度进行排序
     * @param list
     */
    fun sortOrg(list: List<JSONObject>) {
        val comparator = object : Comparator<JSONObject> {
            override fun compare(o1: JSONObject, o2: JSONObject): Int {
                val o1Length = o1.getString("distinguishedName").length
                val o2Length = o2.getString("distinguishedName").length
                return o1Length - o2Length
            }
        }
        Collections.sort(list, comparator)
    }
    /**
    * 获取是否还有下一页
    * @param controls
    * @return
    */
    fun parseControls(controls: Array<Control>?): ByteArray? {

        var cookie: ByteArray? = null

        if (controls != null) {

            for (i in controls.indices) {

                if (controls[i] is PagedResultsResponseControl) {

                    val prrc = controls[i] as PagedResultsResponseControl

                    cookie = prrc.cookie
                }

            }

        }

        return if (cookie == null) ByteArray(0) else cookie

    }

     /**
     * 同步组织机构到数据库
     * @param orgList
     * @throws Exception
     **/

    @Throws(Exception::class)
    fun createOrg(orgList: List<*>) {
        for (i in orgList.indices) {
            val jsonO = orgList[i] as JSONObject

            if (jsonO != null) {
                val org = Organization()
                val distinguishedName = jsonO!!.getString("distinguishedName")
                val level = distinguishedName.substring(0, distinguishedName.indexOf("DC") - 1)
                org.name = jsonO!!.getString("name")
                org.address = if (jsonO!!.getString("street") == null) "" else jsonO!!.getString("street")
                org.phone = if (jsonO!!.getString("phone") == null) "" else jsonO!!.getString("phone")
                org.zipCode = if (jsonO!!.getString("postalCode") == null) "" else jsonO!!.getString("postalCode")
                org.description = if (jsonO!!.getString("description") == null) "" else jsonO!!.getString("description")
                org.levels = level
                org.whenChanged = jsonO!!.getString("whenChanged")
                org.isEnable = "1"
                org.type = "1"
                org.pid = getPid(org)
                var record = Organization()
                record.name=org.name
                record.levels = org.levels
                val list = organizationService!!.select(record)
                if(list.size == 0){
                    organizationService!!.save(org)
                }else if(list.size == 1){
                    org.id = list.get(0).id
                    organizationService!!.update(org);
                }
            }
        }
    }

    fun getPid(org:Organization):String{
        var pid = ""
        var level = org.levels
        if ("" != level && level!!.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray().size == 1) { //levle 有一个元素，说明是顶级节点
            pid = "0"
            logger.debug("当前节点为顶级节点:" + level + "|||当前pid为:" + pid )
        }

        if ("" != level && level.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray().size >= 2) { //levle 有二个元素，说明是第二级节点
            var parentLevel = level.substring(level.indexOf(org.name.toString()) + org.name.toString().length + 1).trim()
            var record = Organization()
            record.levels = parentLevel
            val topOrg = organizationService!!.select(record)
            if (topOrg != null && topOrg.size >0) {
                pid = topOrg!!.get(0).id!!
            } else {
                pid = "0"
                logger.debug("当前节点为二级节点:$level||| 当前pid为0  ?????")
            }
        }

       return  pid
    }


    @Throws(Exception::class)
    fun syncUsers(userName: String, passWord: String) {
            try {
                val start = System.currentTimeMillis()
                val pageSize = 100
                val searchFilter = "objectClass=User"
                val returnedAtts = arrayOf("distinguishedName", "uSNCreated", "whenChanged", "name", "mobile", "department", "sAMAccountName", "mail", "title", "description", "userAccountControl") // 定制返回属性

                val HashEnv = getTable(userName, passWord)
                val ctx = InitialLdapContext(HashEnv, null)
                val searchCtls = SearchControls() //Create the search controls
                searchCtls.searchScope = SearchControls.SUBTREE_SCOPE //Specify the search scope
                val map = globalConfigurService!!.readData()
                val searchBase = if (map.get("adsearchBase") == null) "" else map.get("adsearchBase").toString()
                searchCtls.returningAttributes = returnedAtts //设置返回属性集
                val ctls = arrayOf<Control>(PagedResultsControl(pageSize, false)) // 设置分页读取AD域数据
                ctx.requestControls = ctls
                var cookie: ByteArray? = null
                val arrayList = ArrayList<String>()
                val attrs = ArrayList<Attributes>()
                do {
                    val answer = ctx.search(searchBase, searchFilter, searchCtls)
                    getUserData(answer, attrs)
                    cookie = parseControls(ctx.responseControls)
                    ctx.requestControls = arrayOf<Control>(PagedResultsControl(pageSize, cookie, Control.CRITICAL))
                } while (cookie != null && cookie.size != 0)
                ctx.close()
                if (attrs != null && attrs.size < 2000) {
                    createUsers(attrs)
                } else {
                    val exe = Executors.newFixedThreadPool(attrs.size / 2000 + 1)
                    val list = CopyOnWriteArrayList<List<Attributes>>()
                    for (i in 0..attrs.size / 2000) {
                        list.add(attrs.subList(i * 2000, if (i == attrs.size / 2000) attrs.size else (i + 1) * 2000))
                    }
                    for (l in list) {
                        exe.execute(SyncAnswer(l))
                    }
                    exe.shutdown()
                    while (true) {
                        if (exe.isTerminated) break
                        try {
                            Thread.sleep(3000)
                        } catch (ignored: InterruptedException) {
                        }

                    }
                }
                for (Attrs in attrs) {
                    val ne = Attrs.getAll()
                    while (ne.hasMore()) {
                        val Attr = ne.next() as Attribute
                        if ("sAMAccountName" == Attr.getID().toString()) {
                            arrayList.add(Attr.getAll().nextElement().toString())
                        }
                    }
                }
                //if (arrayList !=null && arrayList.size > 0) syncOrgUserState(arrayList)  //同步AD域和数据库信息状态
                println("总计耗时:" + (System.currentTimeMillis() - start) + "ms")
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }


    @Throws(Exception::class)
    fun getUserData(answer: NamingEnumeration<*>?, list: MutableList<Attributes>) {
        if (answer != null && answer != null) {
            while (answer.hasMoreElements()) {
                val sr = answer.next() as SearchResult
                val Attrs = sr.attributes
                if (Attrs != null) {
                    list.add(Attrs)
                }
            }
        }
    }

    @Throws(Exception::class)
    fun createUsers(list: List<Attributes>) {
        var userList = ArrayList<User>()
        for (Attrs in list) {
            val arrayList = ArrayList<String>()
            val user = User()
            user.password = "123456"
            user.roleId = "4"
            user.isEnable = 1
            user.type = "1"
            if (Attrs != null) {
                val ne = Attrs!!.getAll()
                while (ne.hasMore()) {
                    val Attr = ne.next() as Attribute
                    val AttributeID = Attr.getID().toString()
                    val values = Attr.getAll() // 读取属性值
                    if (values != null) { // 迭代
                        val value = values!!.nextElement().toString()
                        setUserData(user, value, AttributeID, arrayList)   //填充USER
                    }
                }
                userList.add(user)
            }
        }
        if(userList != null && userList.size > 0){
            saveUsers(userList);
        }
    }

  /*  *//**
     * 填充User
     * @param user
     * @param value
     * @param AttributeID
     */
    fun setUserData(user: User, value: String?, AttributeID: String, accounts: MutableList<*>) {
        if ("mobile" == AttributeID) {
            user.phone = value
        }

        if ("name" == AttributeID) {
            user.name = value
        }

        if ("mail" == AttributeID) {
            user.email = value
        }

        if ("sAMAccountName" == AttributeID) {
           user.account = value
        }

        if ("title" == AttributeID) {
           user.position = value
        }

        if ("description" == AttributeID) {
            user.description = value
        }

        if ("whenChanged" == AttributeID) {

        }

        if ("uSNCreated" == AttributeID) {

        }

        if ("userAccountControl" == AttributeID) {
            if (Integer.parseInt(value!!) == 546 || Integer.parseInt(value) == 514 || Integer.parseInt(value) == 66050) {
                user.isEnable = 0
            } else {
                user.isEnable = 1
            }
        }

        if ("distinguishedName" == AttributeID) {
            var valueStr = value!!.replaceFirst("CN=.+?,".toRegex(), "").trim()
            valueStr = valueStr.substring(0,valueStr.indexOf(",DC="))
            val org = organizationService!!.select(Organization(levels = valueStr,type = "1"))
            if (org != null && org.size > 0) {
                user.orgId = org.get(0).id
            }
        }

    }


    /**
     * 把组织结构user插入数据库
     * @param user
     * @throws Exception
     */
    @Throws(Exception::class)
    fun saveUsers(users: ArrayList<User>) {
        //var saveList = ArrayList<User>()
       // var updateList = ArrayList<User>()
        for (user in users){
            var userOld: User? = null
            val userList = userService!!.select(User(account = user.account,type = user.type))
            if (userList != null && userList.size > 0) {
                userOld = userList.get(0)
                NullAwareBeanUtilsBean.copyProperties(userOld,user)
                //updateList.add(userOld)
                userService!!.update(userOld)
            }else {
                userService!!.save(user)
               // saveList.add(user)
            }
        }

    }


    /**
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun enableUser(type:String) {
        userService!!.enableUserByType(type)
        userService!!.disableUserByType(type)
    }



    /*
    @Throws(Exception::class)
    fun syncGroupUsers(passWord: String, userName: String) {
        //查询数据库是否存在安全组根节点  若无 创建新节点(usn="0000")
        var org_id = 0
        val accounts = ArrayList<String>()
        val rootGroup = organizationService.findOrgByUsnCreated("0000")
        if (CommonFunctions.isNull(rootGroup)) {
            val org = Organization()
            org.setIsDelete(0)
            org.setIsEnable(1)
            org.pid = 0
            org.setuSNCreated("0000")
            org.description = "安全组"
            org.name = "默认安全组"
            organizationService.createOrganization(org)
            org_id = org.id!!.toInt()
        } else {
            org_id = rootGroup.id!!.toInt()
        }
        //同步AD域和数据库的信息状态
        val orgs = organizationService.findOrgByUsnCreatedNotNull()
        for (org in orgs) {
            //根节点 与根节点下的子节点 isEnable=1 其他为0
            if ("0000" != org.getuSNCreated() && "0001" != org.getuSNCreated()) {
                org.setIsEnable(0)
            } else {
                org.setIsEnable(1)
            }
            organizationService.updateOrg(org)
        }

        val pageSize = 980
        var cookie: ByteArray? = null

        val members = ArrayList<String>()
        var searchFilter = "objectClass=group"
        val returnedAtts = arrayOf("member")
        val answer = getAnswers(searchFilter, returnedAtts, userName, passWord)
        if (answer != null && answer != null) {
            while (answer.hasMoreElements()) {
                val sr = answer.next() as SearchResult
                val Attrs = sr.attributes
                if (Attrs != null) {
                    val ne = Attrs.all
                    while (ne.hasMore()) {
                        val Attr = ne.next() as Attribute
                        val AttributeID = Attr.getID().toString()
                        val values = Attr.getAll()
                        if (values != null) {
                            while (values!!.hasMoreElements()) {
                                val value = values!!.nextElement().toString()

                                if ("member" == AttributeID) {
                                    val member = value.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                                    members.add(member)
                                }
                            }
                        }
                    }
                }
            }
        }
        for (member in members) {
            searchFilter = member
            val returnedAttss = arrayOf("distinguishedName", "uSNCreated", "whenChanged", "name", "mobile", "department", "sAMAccountName", "mail", "title", "description", "userAccountControl")
            val HashEnv = getTable(userName, passWord)
            val ctx = InitialLdapContext(HashEnv, null)
            val searchCtls = SearchControls()
            searchCtls.searchScope = SearchControls.SUBTREE_SCOPE
            val map = globalConfigurService.readData()
            var searchBase = if (map.get("adsearchBase") == null) "" else map.get("adsearchBase").toString()
            //返回根目录查询用户
            searchBase = searchBase.substring(searchBase.indexOf("DC"), searchBase.length)
            searchCtls.returningAttributes = returnedAttss // 设置返回属性集
            val ctls = arrayOf<Control>(PagedResultsControl(pageSize, false)) // 设置分页读取AD域数据
            ctx.requestControls = ctls
            do {
                val search = ctx.search(searchBase, searchFilter, searchCtls)
                groupMethod(search, org_id, accounts)
                cookie = parseControls(ctx.responseControls)
                ctx.requestControls = arrayOf<Control>(PagedResultsControl(pageSize, cookie, Control.CRITICAL))

            } while (cookie != null && cookie.size != 0)
            ctx.close()
        }

        //同步AD域和数据库信息状态
        syncGroupUserState(accounts)
    }


    *//**
     * 对获取的AD域安全组数据进行循环取出，并执行各个更新方法
     * @param answer
     * @throws Exception
     *//*
    @Throws(Exception::class)
    fun groupMethod(answer: NamingEnumeration<*>?, org_id: Int, accounts: MutableList<*>) {

        if (answer != null && answer != null) {

            while (answer.hasMoreElements()) {
                val sr = answer.next() as SearchResult
                val user = User()
                user.setPassword("123456")
                user.setRoleId(4)
                user.setIs_mycontact(0)
                user.setIsInternalNum(1)
                user.setIs_adGroup_user(1)
                user.setMax_attend(10)
                val Attrs = sr.attributes
                if (Attrs != null) {
                    val ne = Attrs.all
                    while (ne.hasMore()) {

                        val Attr = ne.next() as Attribute
                        val AttributeID = Attr.getID().toString()
                        val values = Attr.getAll() // 读取属性值
                        if (values != null) { // 迭代
                            while (values!!.hasMoreElements()) {
                                val value = values!!.nextElement().toString()
                                createUser(user, value, AttributeID, accounts)   //填充USER
                                user.setOrgId(org_id)
                            }


                        }
                    }
                    //判断数据库中是否存在该安全组用户
                    val groupUsers = userService.queryGroupUserByAccount(user.getAccount())
                    if (CommonFunctions.isEmpty(groupUsers)) {
                        //若为空   添加新安全组用户
                        userService.createGroupUser(user)

                    } else {
                        for (u in groupUsers) {
                            if (!CommonFunctions.isNull(u)) {
                                //if (!u.getWhenChanged().equals(user.getWhenChanged())) {
                                val org = organizationService.retrieveOrganizationById(u.getOrgId())
                                if (!CommonFunctions.isNull(org)) {
                                    user.setOrgId(u.getOrgId())
                                }
                                user.setId(u.getId())
                                user.setRoleId(u.getRoleId())
                                userService.updateUser(user)
                                //}
                            }
                        }
                    }

                }


            }
        }


    }
*/
    internal inner class SyncAnswer(private val list: List<Attributes>) : Thread() {
        override fun run() {
            try {
                createUsers(list)
            } catch (e: Exception) {
                throw CommonException("AD域同步线程出错", e)
            }

        }


    }
}
