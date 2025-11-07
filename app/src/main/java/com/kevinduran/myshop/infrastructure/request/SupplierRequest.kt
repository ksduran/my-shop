package com.kevinduran.myshop.infrastructure.request

import kotlinx.serialization.Serializable

@Serializable
data class SupplierRequest(
    val uuid: String,
    val name: String,
    val debtControl: Int,
    val syncStatus: Int,
    val deleted: Int,
    val raisedBy: String,
    val updatedBy: String,
    val updatedAt: Long,
    val createdAt: Long,
    val license: String
)