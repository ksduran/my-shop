package com.kevinduran.myshop.domain.models

import java.util.UUID

data class SaleReturn(
    val uuid: String = UUID.randomUUID().toString(),
    val storeName: String = "",
    val imagePath: String = "",
    val products: List<SaleReturnProduct> = emptyList(),
    val imageSyncStatus: Int = 0,
    val syncStatus: Int = 0,
    val deleted: Int = 0,
    val updatedAt: Long = 0L,
    val createdAt: Long = 0L
)

data class SaleReturnProduct(
    val productId: String = "",
    val size: String = "",
    val image: String = ""
)

