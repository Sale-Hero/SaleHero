package com.pro.salehero.config.aws

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "aws.ses")
data class AwsProperties(
    val region: String,
    val accessKey: String,
    val secretKey: String,
    val sourceEmail: String
)
