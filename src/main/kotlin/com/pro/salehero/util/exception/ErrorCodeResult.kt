package com.pro.salehero.util.exception

import net.minidev.json.JSONObject

data class ErrorCodeResult(
    val errorCode: ErrorCode,
//    val bindingError: List<FieldBindingError>,
    val bindingError: JSONObject,
    val data: Any
)
