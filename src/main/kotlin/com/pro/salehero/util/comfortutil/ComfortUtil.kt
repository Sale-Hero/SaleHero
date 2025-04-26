package com.pro.salehero.util.comfortutil

import com.pro.salehero.util.exception.CustomException
import com.pro.salehero.util.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class ComfortUtil {

    // 이메일 타입 검증
    fun validateEmail(email: String) {
        val emailRegex = Regex("""^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$""")

        if (email.isBlank()) {
            throw CustomException(ErrorCode.CODE_404)
        }

        if (!emailRegex.matches(email)) {
            throw CustomException(ErrorCode.CODE_4000)
        }
    }

    fun nickNameCreator(): String {
        val firstName = listOf(
            "최상의", "좋은", "영리한", "푸른", "반가운", "잘난", "멋진",
            "화려한", "기쁜", "새로운", "괜찮은", "푸근한", "고마운",
            "젊은", "큰", "놀라운", "당당한", "고른", "한가한",
            "동그란", "넓은", "시원한", "솔직한", "친숙한",
            "귀여운", "친절한", "우월한", "보람찬", "멋진",
            "밝은", "깨끗한", "날랜", "산뜻한", "달콤한", "즐거운",
            "깜찍한", "행복한", "진지한", "흰", "상냥한", "다정한",
            "옳은", "유일한", "잘생긴", "힘찬", "알맞은", "유명한",
            "재밌는", "탐스런", "단호한", "나은", "고운", "건강한",
            "최고의", "든든한", "명랑한", "올바른", "높은", "잘빠진",
            "똑똑한", "빛나는", "활기찬", "따뜻한", "사랑스러운", "환한",
            "풍요로운", "부드러운", "순수한", "훌륭한", "특별한", "지혜로운",
            "성실한", "다재다능한", "열정적인", "헌신적인", "정직한", "믿음직한",
            "창의적인", "대담한", "인상적인", "능숙한", "선명한", "신선한",
            "영광스러운", "품위있는", "자랑스러운", "근사한", "찬란한", "아름다운",
            "우아한", "화사한", "맑은", "화목한", "윤기나는", "싱그러운"
        )

        val middleName = listOf(
            "꿩", "닭", "참새", "생쥐", "곰", "까치", "용", "소", "말", "양", "학", "매",
            "개", "제비", "토끼", "사람", "돼지", "타조", "거위", "오리", "들소", "사자",
            "수달", "가젤", "사슴", "고래", "늑대", "노루", "산양", "여우", "백로", "고니",
            "황새", "앵무", "꿀새", "거북", "고래", "팽귄", "물소", "염소", "영양", "기린",
            "팬더", "비버", "갈매기", "공작", "구관조", "극락조", "금계", "기러기",
            "꾀꼬리", "도요새", "독수리", "두루미", "따오기", "매", "멋쟁이", "문조",
            "물총새", "박새", "백조", "벌새", "비둘기", "삼광조", "새", "솔개", "송골매",
            "원앙", "제비", "집오리", "참새", "파랑새", "팔색조", "펭귄", "학", "홍학",
            "코끼리", "판다", "늑대", "여우", "토끼", "사슴", "호랑이", "금붕어", "거북이",
            "앵무새", "강아지", "고양이", "햄스터", "물범", "순록", "해달", "기린", "코알라",
            "캥거루", "다람쥐", "알파카"
        )

        val lastName = listOf("군", "양", "씨", "님", "공", "후", "장", "선생")

        val shuffledFirstName = firstName.shuffled()
        val shuffledMiddleName = middleName.shuffled()
        val shuffledLastName = lastName.shuffled()

        return "${shuffledFirstName[0]}${shuffledMiddleName[0]}${shuffledLastName[0]}"
    }
}