package com.pro.salehero.util.exception

import net.minidev.json.JSONObject

class CustomException : Exception {
    val code: ErrorCode
    var bindingErrors: List<FieldBindingError> = emptyList()
        private set
    var bindingErrors2: List<FieldBindingError>? = null
        private set
    var data: Any? = null
        private set

    constructor(code: ErrorCode) : super(code.msg) {
        this.code = code
    }

    constructor(code: ErrorCode, data: JSONObject) : super(code.msg) {
        this.code = code
        this.data = data
    }

    constructor(code: ErrorCode, errors: List<FieldBindingError>) : super(code.msg) {
        this.code = code
        this.bindingErrors = errors
    }

    constructor(code: ErrorCode, message: String) : super(message) {
        this.code = code
        this.data = message
    }
}