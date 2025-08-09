package com.pro.salehero.archunit

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices

@AnalyzeClasses(packages = ["com.pro.salehero"])
class ArchitectCycleTest {

    @ArchTest
    val `순환_의존성이_없어야한다` = slices()
        .matching("com.pro.salehero.(**)")
        .should().beFreeOfCycles()
}
