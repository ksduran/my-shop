package com.kevinduran.myshop.infrastructure.request

import kotlinx.serialization.Serializable

@Serializable
data class ProductRequest(
    val uuid: String,
    val name: String,
    val ref: String,
    val salePrice: Int,
    val purchasePrice: Int,
    val supplierName: String,
    val variants: String,
    val imageSyncStatus: Int,
    val syncStatus: Int,
    val deleted: Int,
    val raisedBy: String,
    val updatedBy: String,
    val updatedAt: Long,
    val createdAt: Long,
    val license: String
)