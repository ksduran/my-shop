package com.kevinduran.myshop.infrastructure.request

import kotlinx.serialization.Serializable

@Serializable
data class ProductEntryRequest(
    val uuid: String,
    val productId: String,
    val image: String,
    val syncStatus: Int,
    val deleted: Int,
    val raisedBy: String,
    val updatedBy: String,
    val updatedAt: Long,
    val createdAt: Long,
    val license: String
)