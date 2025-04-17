package com.pro.salehero

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class SaleHeroApplication

fun main(args: Array<String>) {
    runApplication<SaleHeroApplication>(*args)
}
