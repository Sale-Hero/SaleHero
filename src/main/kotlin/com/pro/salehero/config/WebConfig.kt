package com.pro.salehero.config

import com.pro.salehero.auth.security.AdminAuthInterceptor
import com.pro.salehero.auth.security.ApiKeyInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    @Value("\${admin.newsletter.key}") private val apiSecret: String,
    private val adminAuthInterceptor: AdminAuthInterceptor
) : WebMvcConfigurer {

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(ApiKeyInterceptor(apiSecret))
            .addPathPatterns("/api/automate/**") // 자동화
            .addPathPatterns("/api/proxy")
            .addPathPatterns("/test/**") // 테스트용

        // 관리자 권한 체크 인터셉터
        registry.addInterceptor(adminAuthInterceptor)
            .addPathPatterns("/api/admin/**") // 모든 관리자 API에 적용
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:3000",
                "https://salehero.kr",
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}
