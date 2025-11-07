package com.kevinduran.myshop.infrastructure.providers

import com.kevinduran.myshop.domain.providers.UUIDProvider
import java.util.UUID
import javax.inject.Inject

class DefaultUUIDProvider @Inject constructor() : UUIDProvider {
    override fun randomUUID(): String {
        return UUID.randomUUID().toString()
    }
}