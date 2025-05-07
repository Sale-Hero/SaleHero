package com.pro.salehero.config.aws

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ses.SesClient

@Configuration
class AwsConfig(
    private val awsProperties: AwsProperties
) {
    @Bean
    fun sesClient(): SesClient {
        val credentials = AwsBasicCredentials.create(
            awsProperties.accessKey,
            awsProperties.secretKey
        )

        return SesClient.builder()
            .region(Region.of(awsProperties.region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()
    }
}