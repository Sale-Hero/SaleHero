package com.pro.salehero.common

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

@Controller
class EmailTemplateController {

    @GetMapping("/preview-email")
    fun previewEmailTemplate(model: Model): String {
        val templateEngine = setupThymeleafTemplate()

        val context = Context()
        context.setVariable("dealTitle", "테스트 할인 상품")
        context.setVariable("dealId", 1)
        context.setVariable("email", "pnci1029@gmail.com")

        val emailContent = templateEngine.process("saleInformation", context)

        model.addAttribute("emailContent", emailContent)

        return "/saleInformation"
    }

    private fun setupThymeleafTemplate(): SpringTemplateEngine {
        val templateEngine = SpringTemplateEngine()
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.prefix = "templates/"
        templateResolver.suffix = ".html"
        templateResolver.templateMode = TemplateMode.HTML
        templateResolver.characterEncoding = "UTF-8"
        templateEngine.setTemplateResolver(templateResolver)

        return templateEngine
    }
}