package com.kevinduran.myshop.infrastructure.providers

import com.kevinduran.myshop.domain.providers.TimerProvider
import javax.inject.Inject

class SystemTimeProvider @Inject constructor() : TimerProvider {
    override fun getNowMillis(): Long {
        return System.currentTimeMillis()
    }
}