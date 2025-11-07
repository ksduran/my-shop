package com.kevinduran.myshop.domain.providers

interface TimerProvider {
    fun getNowMillis(): Long
}