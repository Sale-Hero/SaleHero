package com.pro.salehero

import com.pro.salehero.config.aws.AwsProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableConfigurationProperties(AwsProperties::class)
@SpringBootApplication
class SaleHeroApplication

fun main(args: Array<String>) {
    runApplication<SaleHeroApplication>(*args)
}
