package com.systec.umeet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@EnableEurekaServer
@SpringBootApplication
class RegistryApplication

fun main(args: Array<String>) {
    runApplication<RegistryApplication>(*args)
}
