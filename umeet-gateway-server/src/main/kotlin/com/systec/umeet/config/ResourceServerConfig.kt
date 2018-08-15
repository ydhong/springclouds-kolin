package com.systec.umeet.config

import com.systec.umeet.properties.PermitAllUrlProperties
import com.systec.umeet.security.CustomRemoteTokenServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer

/**
 * @author super.wu
 */
@Configuration
@EnableResourceServer
class ResourceServerConfig : ResourceServerConfigurerAdapter() {

    @Autowired
    private val loadBalancerClient: LoadBalancerClient? = null

    @Autowired
    private val resource: ResourceServerProperties? = null

    @Autowired
    private val permitAllUrlProperties: PermitAllUrlProperties? = null


    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
                .requestMatchers().antMatchers("/**")
                .and()
                .authorizeRequests()
                .antMatchers(*permitAllUrlProperties!!.permitallPatterns).permitAll()
                .anyRequest().authenticated()
    }

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        val resourceServerTokenServices = CustomRemoteTokenServices()
        resourceServerTokenServices.setCheckTokenEndpointUrl(resource!!.tokenInfoUri)
        resourceServerTokenServices.setClientId(resource.clientId)
        resourceServerTokenServices.setClientSecret(resource.clientSecret)
        if (loadBalancerClient != null) {
            resourceServerTokenServices.setLoadBalancerClient(loadBalancerClient)
        }
        resources!!.tokenServices(resourceServerTokenServices)
    }

}
