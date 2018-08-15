package com.umeet.conference.web;
import com.umeet.conference.contants.Constant
import com.umeet.conference.model.User
import com.umeet.conference.service.UserService
import com.umeet.conference.util.EncryptUtil
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * 公共控制器
 * @author leo
 *
 */
open class CommonController {
	@Autowired
	private var userService: UserService? = null;


	/**
	 * 获取当前登录用户
	 * @return
	 */
	fun getCurrentUser(): User?{
		val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).getRequest();
		val authKey = request.getHeader(Constant.AUTH_KEY);
		if(StringUtils.isNotBlank(authKey)) {
			val decryptAuthKey = EncryptUtil.decryptBase64(authKey, Constant.SECRET_KEY);
			val auths = decryptAuthKey.split("&&&");
			val username = auths[0];
			val password = auths[1];
			val record = User();
			record.account=username;
			record.password=password;
			return userService!!.selectOne(record);
		}
		return null;
	}


}
