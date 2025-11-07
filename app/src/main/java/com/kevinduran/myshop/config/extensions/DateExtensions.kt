package com.kevinduran.myshop.config.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

fun Long.toDateString(pattern: String = "EEEE, dd MMM yyyy") : String {
    val instant = Instant.ofEpochMilli(this)
    val zoneId = ZoneId.systemDefault()
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return formatter.format(instant.atZone(zoneId))
}

fun Long.toHumanDate() : String {
    val currentDate = LocalDate.now()
    val dateTime = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault())
    val date = dateTime.toLocalDate()
    val time = dateTime.toLocalTime()

    return when {
        date.isEqual(currentDate) -> {
            val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
            time.format(formatter)
        }
        date.isEqual(currentDate.minusDays(1)) -> "ayer"
        date.isEqual(currentDate.minusDays(2)) -> "antier"
        else -> {
            val formatter = DateTimeFormatter.ofPattern("d MMM", Locale.getDefault())
            dateTime.format(formatter).replace(".", "").lowercase()
        }
    }
}

fun Long.startOfDay(): Long {
    val millis = this
    return Calendar.getInstance().apply {
        timeInMillis = millis
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

fun Long.endOfDay(): Long {
    val millis = this
    return Calendar.getInstance().apply {
        timeInMillis = millis
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }.timeInMillis
}

fun Long.correctToLocalStartOfDayMillis(): Long {
    val localDate = Instant.ofEpochMilli(this)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
    return localDate
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}





