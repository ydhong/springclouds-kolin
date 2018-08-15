package com.systec.umeet.security


import com.systec.umeet.constants.SecurityConstants
import org.apache.commons.logging.LogFactory
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.AccessTokenConverter
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices
import org.springframework.util.Assert
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

import java.io.IOException
import java.io.UnsupportedEncodingException
import java.util.Base64


class CustomRemoteTokenServices : ResourceServerTokenServices {

    private var loadBalancerClient: LoadBalancerClient? = null

    protected val logger = LogFactory.getLog(javaClass)

    private var restTemplate: RestOperations? = null

    private var checkTokenEndpointUrl: String? = null

    private var clientId: String? = null

    private var clientSecret: String? = null

    private var tokenName = "token"

    private var tokenConverter: AccessTokenConverter = DefaultAccessTokenConverter()

    init {
        restTemplate = RestTemplate()
        (restTemplate as RestTemplate).errorHandler = object : DefaultResponseErrorHandler() {
            @Throws(IOException::class)
            override// Ignore 400
            fun handleError(response: ClientHttpResponse) {
                if (response.rawStatusCode != 400) {
                    super.handleError(response)
                }
            }
        }
    }

    fun setRestTemplate(restTemplate: RestOperations) {
        this.restTemplate = restTemplate
    }

    fun setCheckTokenEndpointUrl(checkTokenEndpointUrl: String) {
        this.checkTokenEndpointUrl = checkTokenEndpointUrl
    }

    fun setClientId(clientId: String) {
        this.clientId = clientId
    }

    fun setClientSecret(clientSecret: String) {
        this.clientSecret = clientSecret
    }

    fun setAccessTokenConverter(accessTokenConverter: AccessTokenConverter) {
        this.tokenConverter = accessTokenConverter
    }

    fun setTokenName(tokenName: String) {
        this.tokenName = tokenName
    }

    @Throws(AuthenticationException::class, InvalidTokenException::class)
    override fun loadAuthentication(accessToken: String): OAuth2Authentication {

        val formData = LinkedMultiValueMap<String, String>(1)
        formData.add(tokenName, accessToken)
        val headers = HttpHeaders()
        headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret))

        val serviceInstance = loadBalancerClient!!.choose(SecurityConstants.AUTH_SERVICE)
                ?: throw RuntimeException("Failed to choose an auth instance.")

        val map = postForMap(serviceInstance.uri.toString() + checkTokenEndpointUrl!!, formData, headers)

        if (map!!.containsKey("error")) {
            logger.debug("check_token returned error: " + map["error"])
            throw InvalidTokenException(accessToken)
        }

        Assert.state(map.containsKey("client_id"), "Client id must be present in response from auth server")
        return tokenConverter.extractAuthentication(map)
    }

    fun setLoadBalancerClient(loadBalancerClient: LoadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient
    }

    override fun readAccessToken(accessToken: String): OAuth2AccessToken {
        throw UnsupportedOperationException("Not supported: read access token")
    }

    private fun getAuthorizationHeader(clientId: String?, clientSecret: String?): String {
        val creds = String.format("%s:%s", clientId, clientSecret)
        try {
            return StringBuilder("Basic ").append(Base64.getEncoder().encodeToString(creds.toByteArray(charset("utf-8")))).toString()
        } catch (e: UnsupportedEncodingException) {
            throw IllegalStateException("Could not convert String")
        }

    }

    private fun postForMap(path: String, formData: MultiValueMap<String, String>, headers: HttpHeaders): Map<String, Any>? {
        if ("".equals(headers.contentType)) {
            headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        }
        return restTemplate!!.exchange(path, HttpMethod.POST, HttpEntity(formData, headers), Map::class.java).body as Map<String, Any>?
    }

}
