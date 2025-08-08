plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.10"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("kapt") version "1.9.25"
}

group = "com.pro"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

val querydslVersion = "5.0.0" // Querydsl 버전 지정

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-validation")


    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // QueryDSL 의존성 추가
    implementation("com.querydsl:querydsl-jpa:$querydslVersion:jakarta")
    kapt("com.querydsl:querydsl-apt:$querydslVersion:jakarta")

    // open ai
    implementation("com.aallam.openai:openai-client:3.3.0")
    implementation("io.ktor:ktor-client-okhttp:2.3.1")

    // suspend
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")

    // google smtp
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // thymeleaf
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // aws ses
    implementation("software.amazon.awssdk:ses:2.25.40")

    // html parsing
    implementation("org.jsoup:jsoup:1.16.2")

    // proxy용 client
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // test
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
//    testImplementation ("it.ozimov:embedded-redis:0.7.3")

    // map struct
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")

    // arch unit test
    testImplementation("com.tngtech.archunit:archunit-junit5:1.2.1")


}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

// Querydsl Q클래스 생성 경로 설정
sourceSets {
    main {
        kotlin {
            srcDir("$buildDir/generated/source/kapt/main")
        }
    }
}

// kapt 설정 추가
kapt {
    correctErrorTypes = true
    javacOptions {
        option("querydsl.entityAccessors", "true")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
