package com.systec.umeet.config

import com.systec.umeet.security.HeaderEnhanceFilter
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import javax.servlet.Filter

/**
 * @author super.wu
 */
@Configuration
@EnableAutoConfiguration
class FilterConfig {

    @Bean
    fun filterRegistrationBean(): Any{
        val registrationBean = FilterRegistrationBean<Filter>()
        registrationBean.filter = headerEnhanceFilter()
        registrationBean.order = Ordered.HIGHEST_PRECEDENCE
        return registrationBean
    }

    @Bean
    fun headerEnhanceFilter(): HeaderEnhanceFilter {
        return HeaderEnhanceFilter()
    }

}
