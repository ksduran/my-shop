package com.kevinduran.myshop.domain.models

import java.util.UUID

data class ProductEntry(
    val uuid: String = UUID.randomUUID().toString(),
    val productId: String = "",
    val image: String = "",
    val syncStatus: Int = 0,
    val deleted: Int = 0,
    val raisedBy: String = "",
    val updatedBy: String = "",
    val updatedAt: Long = 0L,
    val createdAt: Long = 0L,
)
