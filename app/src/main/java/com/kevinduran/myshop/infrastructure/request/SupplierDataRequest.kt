package com.kevinduran.myshop.infrastructure.request

import kotlinx.serialization.Serializable

@Serializable
data class SupplierDataRequest(
    val uuid: String,
    val supplierId: String,
    val productId: String,
    val title: String,
    val quantity: Int,
    val image: String,
    val purchasePrice: Int,
    val type: String,
    val syncStatus: Int,
    val deleted: Int,
    val raisedBy: String,
    val updatedBy: String,
    val updatedAt: Long,
    val createdAt: Long,
    val license: String
)