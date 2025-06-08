package com.pro.salehero.config

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionHelper {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun <T> executeInNewTransaction(block: () -> T): T { // 영속송 커밋용 새 트랜잭션 생성 함수
        return block()
    }
}
