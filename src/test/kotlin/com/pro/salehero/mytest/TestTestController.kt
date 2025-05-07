package com.pro.salehero.mytest

import org.assertj.core.api.Assertions
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RssCollectorTest {

    @Test
    fun testRssCollector() {
        val rssUrl = "https://www.google.co.kr/alerts/feeds/16143539459920574796/5081454741560405721"

        try {
            val doc: Document = Jsoup.connect(rssUrl).get()
            val entries: Elements = doc.select("entry") // item 대신 entry 사용
            for (entry in entries) {
                val title = entry.select("title").text()
                val link = entry.select("link").attr("href") // link의 href 속성 사용
                val published = entry.select("published").text() // pubDate 대신 published 사용
                val content = entry.select("content").text()

                Assertions.assertThat(title).isNotNull()
                Assertions.assertThat(link).isNotNull()
                Assertions.assertThat(published).isNotNull()
                Assertions.assertThat(content).isNotNull()
            }
        } catch (e: Exception) {
            println("에러 발생: ${e.message}")
            e.printStackTrace()
        }
    }
}