package com.systec.umeet.config

import com.systec.umeet.properties.PermitAllUrlProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author keets
 * @date 2017/9/29
 */
@Configuration
class ServiceConfig {


    @Value("\${server.port}")
    private val securePort: Int = 0


    val permitAllUrlProperties: PermitAllUrlProperties
        @Bean
        @ConfigurationProperties(prefix = "auth")
        get() = PermitAllUrlProperties()

}
