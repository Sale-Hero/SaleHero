package com.pro.salehero.config

import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext

@MockBean(JpaMetamodelMappingContext::class)
abstract class IntegrationControllerTestSupport {
}
