package com.pro.salehero.common.controller
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.ContentType
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URLDecoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

@RestController
@RequestMapping("/api/proxy")
class ProxyController {

    @GetMapping
    fun proxy(@RequestParam url: String): ResponseEntity<String> {
        val decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8)
        val client: CloseableHttpClient = HttpClients.createDefault()
        val request = HttpGet(decodedUrl)

        request.addHeader("User-Agent", "Mozilla/5.0")
        request.addHeader("Accept-Encoding", "gzip, deflate, br")

        println("decodedUrl = ${decodedUrl}")
        return try {
            client.execute(request).use { response ->
                val entity = response.entity
                val contentTypeStr = entity.contentType
                val contentType = ContentType.parse(contentTypeStr)

                val charset: Charset = contentType.charset ?: StandardCharsets.UTF_8

                val bytes = EntityUtils.toByteArray(entity)
                val decodedBody = String(bytes, charset)

                ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(decodedBody)

            }
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body("실패: ${e.message}")
        }
    }
}