package se.supernovait.anya.core.domain.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearsUntil
import kotlin.time.Clock
import kotlin.time.Instant

fun Clock.Companion.currentTimeMilliseconds(): Long {
    return Clock.System.now().toEpochMilliseconds()
}

fun LocalDateTime.Companion.now(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}

fun LocalDate.Companion.now(): LocalDate {
    return LocalDateTime.now().date
}

fun LocalTime.Companion.now(): LocalTime {
    return LocalDateTime.now().time
}

fun LocalDate?.isoString(): String {
    return this?.toString() ?: ""
}

fun LocalDate?.yearsUntilNow(): Int {
    return this?.yearsUntil(LocalDate.now()) ?: 0
}

fun LocalDate?.monthsUntilNow(): Int {
    return this?.monthsUntil(LocalDate.now()) ?: 0
}

fun String?.toLocalDate(): LocalDate? {
    return if(this.isNullOrBlank()) null else LocalDate.parse(this)
}

fun Long.toLocalDate(): LocalDate {
    return Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
}
