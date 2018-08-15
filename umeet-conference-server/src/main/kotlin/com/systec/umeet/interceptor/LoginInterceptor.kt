package com.umeet.conference.interceptor;
import com.umeet.conference.contants.Constant
import com.umeet.conference.model.User
import com.umeet.conference.service.UserService
import com.umeet.conference.util.EncryptUtil
import com.umeet.conference.util.FastJsonUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@Component("loginInterceptor")
class LoginInterceptor : HandlerInterceptorAdapter() {

    @Autowired
    private var userService: UserService? = null;

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        val session: HttpSession = request.getSession();


        //检查账号有效性
        var sessionUser = session.getAttribute(Constant.LOGIN_ADMIN_USER) as User?;
        if (sessionUser == null) {
            println("请求url:" + request.getRequestURI());
            println("header auth key:" + request.getHeader(Constant.AUTH_KEY));
            val authKey = getAuthKey(request)
            //val sessionId = request.getHeader(Constant.SESSION_ID);
            // 校验sessionid和authKey
            if (StringUtils.isEmpty(authKey) /*|| StringUtils.isEmpty(sessionId)*/) {
                response.contentType = "application/json;charset=UTF-8";
                val writer = response.getWriter();
                writer.write(FastJsonUtils.resultError(-100, "authKey或sessionId不能为空！", null));
                writer.flush();
                return false;
            }
            val decryptAuthKey = EncryptUtil.decryptBase64(authKey, Constant.SECRET_KEY);
            val auths = decryptAuthKey.split("&&&");
            val username = auths[0];
            val password = auths[1];
            val record = User();

            record.account = username;
            record.password = password;
            sessionUser = userService!!.selectOne(record);
            //设置登录用户id
            session.setAttribute(Constant.LOGIN_ADMIN_USER, sessionUser);
        }

        if (sessionUser == null || sessionUser.isEnable == 1) {
            response.contentType = "application/json;charset=UTF-8";
            val writer = response.getWriter();
            writer.write(FastJsonUtils.resultError(-101, "账号已被删除或禁用", null));
            writer.flush();
            return false;
        }

        return true;
    }

    fun getAuthKey(request: HttpServletRequest):String{
        var authKey = ""
        var cokies = request.cookies
        for(cookie in cokies){
            if("authKey".equals(cookie.name)){
                authKey = cookie.value
            }
        }
        return authKey
    }
}
