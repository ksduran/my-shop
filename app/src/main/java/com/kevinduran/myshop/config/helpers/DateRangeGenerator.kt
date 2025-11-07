package com.kevinduran.myshop.config.helpers

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

object DateRangeGenerator {

    private val zone = ZoneId.systemDefault()

    fun today(): Pair<Long, Long> {
        val now = LocalDate.now()
        return Pair(
            now.atStartOfDay().atZone(zone).toInstant().toEpochMilli(),
            now.atTime(LocalTime.MAX).atZone(zone).toInstant().toEpochMilli()
        )
    }

    fun thisWeek(): Pair<Long, Long> {
        val now = LocalDate.now()
        val startOfWeek = now.with(DayOfWeek.MONDAY)
        val endOfWeek = now.with(DayOfWeek.SUNDAY)
        return Pair(
            startOfWeek.atStartOfDay().atZone(zone).toInstant().toEpochMilli(),
            endOfWeek.atTime(LocalTime.MAX).atZone(zone).toInstant().toEpochMilli()
        )
    }

    fun thisMonth(): Pair<Long, Long> {
        val now = LocalDate.now()
        val startOfMonth = now.withDayOfMonth(1)
        val endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth())
        return Pair(
            startOfMonth.atStartOfDay().atZone(zone).toInstant().toEpochMilli(),
            endOfMonth.atTime(LocalTime.MAX).atZone(zone).toInstant().toEpochMilli()
        )
    }

    fun thisYear(): Pair<Long, Long> {
        val now = LocalDate.now()
        val startOfYear = now.withDayOfYear(1)
        val endOfYear = now.with(TemporalAdjusters.lastDayOfYear())
        return Pair(
            startOfYear.atStartOfDay().atZone(zone).toInstant().toEpochMilli(),
            endOfYear.atTime(LocalTime.MAX).atZone(zone).toInstant().toEpochMilli()
        )
    }
}