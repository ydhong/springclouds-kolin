package com.systec.umeet.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.zuul.context.RequestContext
import com.systec.umeet.constants.SecurityConstants
import com.systec.umeet.properties.PermitAllUrlProperties
import org.apache.commons.codec.binary.Base64
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import java.io.IOException
import java.util.*

/**
 * @author super.wu
 */
class HeaderEnhanceFilter : Filter {

    @Autowired
    private val permitAllUrlProperties: PermitAllUrlProperties? = null

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        var authorization = (servletRequest as HttpServletRequest).getHeader(AuthorizationKey)
        val requestURI = servletRequest.requestURI
        // test if request url is permit all , then remove authorization from header
        LOGGER.info(String.format("Enhance request URI : %s.", requestURI))
        if (isPermitAllUrl(requestURI) && isNotOAuthEndpoint(requestURI)) {
            val resetRequest = removeValueFromRequestHeader(servletRequest)
            filterChain.doFilter(resetRequest, servletResponse)
            return
        } else {
            if (StringUtils.isNotEmpty(authorization)) {
                // 判断是否是jwt token
                if (isJwtBearerToken(authorization)) {
                    try {
                        authorization = StringUtils.substringBetween(authorization, ".")
                        val decoded = String(Base64.decodeBase64(authorization))

                        val properties = ObjectMapper().readValue(decoded, Map::class.java)

                        val userId = properties[SecurityConstants.USER_ID_IN_HEADER] as String

                        RequestContext.getCurrentContext().addZuulRequestHeader("userId", userId)
                        RequestContext.getCurrentContext().addZuulRequestHeader("username", properties["user_name"] as String)
                        RequestContext.getCurrentContext().addZuulRequestHeader("roles", properties["roles"] as String)

                    } catch (e: Exception) {
                        e.printStackTrace()
                        LOGGER.error("Failed to customize header for the request, but still release it as the it would be regarded without any user details.", e)
                    }

                }
            } else {
                LOGGER.info("Regard this request as anonymous request, so set anonymous user_id in the header.")
                RequestContext.getCurrentContext().addZuulRequestHeader(SecurityConstants.USER_ID_IN_HEADER, ANONYMOUS_USER_ID)
            }
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

    override fun destroy() {

    }

    private fun isJwtBearerToken(token: String): Boolean {
        return StringUtils.countMatches(token, ".") == 2 && (token.startsWith("Bearer") || token.startsWith("bearer"))
    }

    private fun isNotOAuthEndpoint(requestURI: String): Boolean {
        return !requestURI.contains("/login")
    }

    private fun removeValueFromRequestHeader(request: HttpServletRequest): HttpServletRequestWrapper {
        return object : HttpServletRequestWrapper(request) {
            private var headerNameSet: MutableSet<String>? = null
            private var headerSet: MutableSet<String>? = null

            override fun getHeaderNames(): Enumeration<String> {
                if (headerNameSet == null) {
                    // first time this method is called, cache the wrapped request's header names:
                    headerNameSet = HashSet()
                    val wrappedHeaderNames = super.getHeaderNames()
                    while (wrappedHeaderNames.hasMoreElements()) {
                        val headerName = wrappedHeaderNames.nextElement()
                        if (!AuthorizationKey.equals(headerName, ignoreCase = true)) {
                            headerNameSet!!.add(headerName)
                        }
                    }
                    //set default header name value of tenant id and user id
                    headerNameSet!!.add(SecurityConstants.USER_ID_IN_HEADER)
                }

                return Collections.enumeration(headerNameSet!!)
            }

            override fun getHeaders(name: String): Enumeration<String> {

                if (AuthorizationKey.equals(name, ignoreCase = true)) {
                    return Collections.emptyEnumeration()
                }
                if (SecurityConstants.USER_ID_IN_HEADER.equals(name, ignoreCase = true)) {
                    headerSet = HashSet()
                    headerSet!!.add(ANONYMOUS_USER_ID)
                    return Collections.enumeration(headerSet!!)
                }
                return super.getHeaders(name)
            }

            override fun getHeader(name: String): String? {
                if (AuthorizationKey.equals(name, ignoreCase = true)) {
                    return null
                }
                return if (SecurityConstants.USER_ID_IN_HEADER.equals(name, ignoreCase = true)) {
                    ANONYMOUS_USER_ID
                } else super.getHeader(name)
            }
        }
    }

    private fun isPermitAllUrl(url: String): Boolean {
        return permitAllUrlProperties!!.isPermitAllUrl(url)
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(HeaderEnhanceFilter::class.java)

        private val ANONYMOUS_USER_ID = "d4a65d04-a5a3-465c-8408-405971ac3346"
        private val AuthorizationKey = "Authorization"
    }

}
