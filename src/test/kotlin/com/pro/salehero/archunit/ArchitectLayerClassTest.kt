package com.pro.salehero.archunit

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes

@AnalyzeClasses(packages = ["com.pro.salehero"])
class ArchitectLayerClassTest {

    /**
     * 레이어별 클래스 규칙
     */
    @ArchTest
    val `서비스_파일들은_당연히_서비스_피키지_안에_있어야한다` =
        classes()
            .that().haveNameMatching(".*Service")
            .should().resideInAPackage("..service..")

    @ArchTest
    val `컨트롤러_놈들은_컨트롤러_패키지_안에_있어야한다` =
        classes()
            .that().haveNameMatching(".*Controller")
            .should().resideInAPackage("..controller..")

    @ArchTest
    val `리포지토리는_도메인_패키지_안에_있어야한다` =
        classes()
            .that().haveNameMatching(".*Repository")
            .should().resideInAPackage("..domain..")
}
