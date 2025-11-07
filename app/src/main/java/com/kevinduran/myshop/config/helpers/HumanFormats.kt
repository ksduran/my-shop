package com.kevinduran.myshop.config.helpers

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class HumanFormats {

    companion object {
        fun milliToStringDate(milliEpoch: Long) : String {
            val now = Instant.now()
            val date = Instant.ofEpochMilli(milliEpoch)
            val daysAgo = ChronoUnit.DAYS.between(date, now)
            var pattern = ""
            var dateString = ""
            when (daysAgo) {
                0L -> pattern = "HH:mm"
                1L -> dateString = "Ayer"
                2L -> dateString = "Antier"
                else -> pattern = "dd MMM"
            }
            return if(pattern.isEmpty()) {
                dateString
            } else {
                DateTimeFormatter.ofPattern(pattern)
                    .withZone(ZoneId.systemDefault()).format(date)
            }
        }
    }

}