package com.pro.salehero.subscribe.domain.weekday

import com.pro.salehero.subscribe.domain.DayOfWeek
import com.pro.salehero.util.CreateAndUpdateAudit
import com.pro.salehero.util.CreateAudit
import jakarta.persistence.*

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(columnNames = ["subscriberId", "days"])]
)
data class WeekDay(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val subscriberId: Long,

    @Enumerated(EnumType.STRING)
    val days: DayOfWeek,
):CreateAudit()
