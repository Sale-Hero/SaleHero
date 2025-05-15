package com.pro.salehero.admins.rawnewsletter.controller

import com.pro.salehero.admins.rawnewsletter.service.RawNewsLetterService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController @RequestMapping("/api/admin/raw")
class RawNewsLetterController (
    private val rawNewsLetterService: RawNewsLetterService
){
}