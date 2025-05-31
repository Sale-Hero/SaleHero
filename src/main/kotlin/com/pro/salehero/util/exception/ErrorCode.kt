package com.pro.salehero.util.exception


import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(
    val httpCode: Int,
    val msg: String
) {
    CODE_200(200, "Success"),
    CODE_404(404, "존재하지 않는 데이터"),
    CODE_4041(404, "잘못된 요청입니다."),
    CODE_4042(404, "삭제된 데이터입니다."),
    CODE_4043(404, "값이 비어있습니다."),
    CODE_403(403, "인증 정보가 없습니다."),
    CODE_4031(403, "유효하지 않은 토큰입니다."),
    CODE_4032(403, "리프레시 토큰이 만료되었습니다."),

    // 구독 관련
    CODE_4000(404, "유효하지 않은 이메일 타입"),
    CODE_4001(404, "이미 구독중인 이메일입니다."),
    CODE_4002(404, "인증코드가 일치하지 않습니다."),

    ;

    companion object {
        fun getErrorCodeByCode(httpCode: Int): ErrorCode {
            return values().first { it.httpCode == httpCode }
        }
    }
}