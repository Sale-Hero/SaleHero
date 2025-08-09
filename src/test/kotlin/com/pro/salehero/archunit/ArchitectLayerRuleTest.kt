package com.pro.salehero.archunit

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

@AnalyzeClasses(
    packages = ["com.pro.salehero"],
    importOptions = [ImportOption.DoNotIncludeTests::class] // 테스트  패키지는 포함 X
)
class ArchitectLayerRuleTest {
    /**
     * 레이어간 의존성 규칙 테스트
     * * ~의 클래스 파일들이
     * ..~..이름의 패키지 안에서 의존성 주입을 위해 호출되는지 테스트
     */
    @ArchTest
    val `리포지토리는_서비스_혹은_컨피그_디렉토리_하위에서_의존성주입을받아야한다` = classes()
        .that().haveNameMatching(".*Repository")
        .should().onlyBeAccessed().byAnyPackage("..service..", "..config..")

    @ArchTest
    val `리포지토리는_컨트롤러패키지_하위에서_의존성을_주입받으면_안된다` = noClasses()
        .that().haveNameMatching(".*Repository")
        .should().onlyBeAccessed().byAnyPackage("..controller..")

    @ArchTest
    val `서비스는_컨트롤러에서만_사용되어야한다` = classes()
        .that().haveNameMatching(".*Service")
        .should().onlyBeAccessed().byAnyPackage("..service..", "..controller..", "..scheduling..")

}
