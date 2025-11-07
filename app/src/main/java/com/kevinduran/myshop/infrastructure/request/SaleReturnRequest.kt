package com.kevinduran.myshop.infrastructure.request

import kotlinx.serialization.Serializable

@Serializable
data class SaleReturnRequest(
    val uuid: String,
    val storeName: String,
    val imagePath: String,
    val products: String,
    val imageSyncStatus: Int,
    val syncStatus: Int,
    val deleted: Int,
    val updatedAt: Long,
    val createdAt: Long,
    val license: String
)
