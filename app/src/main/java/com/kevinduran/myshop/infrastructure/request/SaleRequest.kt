package com.kevinduran.myshop.infrastructure.request

import kotlinx.serialization.Serializable

@Serializable
data class SaleRequest(
    val uuid: String,
    val productId: String,
    val companyName: String,
    val supplierId: String,
    val color: String,
    val size: String,
    val sizeR: String,
    val changedProductId: String,
    val changedSize: String,
    val changedProductColor: String,
    val salePrice: Int,
    val purchasePrice: Int,
    val raisedBy: String,
    val updatedBy: String,
    val paymentStatus: String,
    val image: String,
    val syncStatus: Int,
    val deleted: Int,
    val updatedAt: Long,
    val createdAt: Long,
    val license: String
)