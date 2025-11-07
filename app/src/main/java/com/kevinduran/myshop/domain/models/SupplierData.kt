package com.kevinduran.myshop.domain.models

import java.util.UUID

data class SupplierData(
    val uuid : String = UUID.randomUUID().toString(),
    val supplierId: String = "",
    val productId: String = "",
    val title: String = "",
    val quantity: Int = 0,
    val image: String = "",
    val purchasePrice: Int = 0,
    val type: String = "INCOME",
    val syncStatus: Int = 0,
    val deleted: Int = 0,
    val raisedBy: String = "",
    val updatedBy: String = "",
    val updatedAt: Long = 0L,
    val createdAt: Long = 0L
)
