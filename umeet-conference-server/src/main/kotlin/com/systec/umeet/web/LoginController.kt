package com.umeet.conference.web;
import com.google.code.kaptcha.impl.DefaultKaptcha
import com.umeet.conference.contants.Constant
import com.umeet.conference.model.User
import com.umeet.conference.service.UserService
import com.umeet.conference.util.EncryptUtil
import com.umeet.conference.util.FastJsonUtils
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.imageio.ImageIO
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import sun.misc.BASE64Decoder
import java.io.IOException


/**
 * 登录控制层
 * @author leo.aqing
 *
 */
@RestController
@RequestMapping("/umeet")
@Api(value = "LoginController", description="登录接口")
class LoginController : CommonController(){
	@Autowired
	private var  userService: UserService? = null;
	@Autowired
	private var captchaProducer: DefaultKaptcha? = null;

	/**
	 * 登录
	 * @param record
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "登录", notes = "登录")
	//@ApiImplicitParams(value= arrayOf(@ApiImplicitParam(name = "record", required=true, dataType = "SysAdminUser")))
	@PostMapping(value = "/loggin", produces = arrayOf("application/json;charset=UTF-8"))
	fun login(@RequestBody  record: User, request: HttpServletRequest,response: HttpServletResponse): String {
		val data = mutableMapOf<String, Any>();
		if(StringUtils.isBlank(record.account)) {
			return FastJsonUtils.resultError(-100, "账号不能为空", null);
		}
		record.account = decoding(record.account!!)
		record.password = decoding(record.password!!)
		//record.password= DigestUtils.md5Hex(record.password);
		val user: User? = userService?.selectOne(record);
		if(user?.toString()==null) {
			return FastJsonUtils.resultError(-100, "帐号与密码错误不正确", null);
		}
		if(user?.isEnable?.compareTo(1) == 1 ) {
			return FastJsonUtils.resultError(-100, "帐号已被禁用", null);
		}
		val authKey = EncryptUtil.encryptBase64(user?.account+"&&&"+user?.password, Constant.SECRET_KEY);

		response.addHeader("authKey",authKey)
		response.addHeader("sessionId",request.getSession().getId())
		request.getSession().setAttribute(Constant.LOGIN_ADMIN_USER,user)

		val cookie = Cookie("authKey", authKey)
		cookie.maxAge = -1
		cookie.path = "/"
		response.addCookie(cookie)
		// 返回信息
		data.put("rememberKey", authKey)
		data.put("authKey", authKey)
		data.put("sessionId", request.getSession().getId())
		user?.password = ""
		data.put("userInfo", user!!)

		return FastJsonUtils.resultSuccess(200, "登录成功", data)
	}

	/**
	 * 用户登录账户、密码解密
	 * @param str
	 * @return
	 */
	fun decoding(str: String): String {
		var strs = ""
		try {
			val base64en = BASE64Decoder()
			val baseName = base64en.decodeBuffer(str)
			strs = String(baseName)
		} catch (e: IOException) {
			e.printStackTrace()
		}

		return strs
	}

	/**
	 * 重新登录
	 * @param rememberKey
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "重新登录", notes = "", httpMethod = "POST")
	//@ApiImplicitParams(value = arrayOf(@ApiImplicitParam(name = "rememberKey", value ="登录成功后的授权码", required = true, dataType = "String")))
	@RequestMapping(value = "/relogin", produces = arrayOf("application/json;charset=UTF-8"))
	fun  reLogin(rememberKey: String, request: HttpServletRequest): String {
		val rememberValue = EncryptUtil.decryptBase64(rememberKey, Constant.SECRET_KEY);
		val splits = rememberValue.split("|")
		val record = User()
		record.account=splits[0]
		record.password=splits[1]
		var user = userService?.selectOne(record)
		if(user?.toString() == null) {
			return FastJsonUtils.resultError(-400, "重新登录失败", null)
		}
		return FastJsonUtils.resultSuccess(200, "重新登录成功", null)
	}

	/**
	 * 登出
	 * @param session
	 * @return
	 */
	@ApiOperation(value = "登出", notes = "")
	@PostMapping(value = "/logout", produces = arrayOf("application/json;charset=UTF-8"))
	@ResponseBody
	fun logout(session: HttpSession ): String{
		session.invalidate();
		return FastJsonUtils.resultSuccess(200, "退出成功", null);
	}

	/***
	 * 验证码
	 */
	@ApiOperation(value = "验证码", notes = "")
	@GetMapping(value = "/verify")
	fun verify( request: HttpServletRequest,  response: HttpServletResponse){
		response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control",
                "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        val capText = captchaProducer!!.createText();
        println("capText: " + capText);

        try {
            val uuid=UUID.randomUUID().toString();
            //redisTemplate.opsForValue().set(uuid, capText,60*5,TimeUnit.SECONDS);
            val cookie = Cookie("captchaCode",uuid);
            response.addCookie(cookie);
        } catch (e: Exception ) {
            e.printStackTrace();
        }

        val bi = captchaProducer?.createImage(capText);
        val out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
	}

}
