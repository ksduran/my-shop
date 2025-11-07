package com.kevinduran.myshop.domain.models

import com.kevinduran.myshop.config.constants.Payment
import java.util.UUID

data class Sale(
    val uuid : String = UUID.randomUUID().toString(),
    val productId : String = "",
    val companyName : String = "",
    val supplierId : String = "",
    val color : String = "",
    val size : String = "",
    val sizeR: String = "",
    val changedProductId: String = "",
    val changedSize: String = "",
    val changedProductColor: String = "",
    val salePrice : Int = 0,
    val purchasePrice : Int = 0,
    val raisedBy : String = "@Admin",
    val updatedBy : String = "@Admin",
    val paymentStatus : String = Payment.ByRaised.status,
    val image : String = "",
    val syncStatus: Int = 0,
    val deleted: Int = 0,
    val updatedAt: Long = 0L,
    val createdAt: Long = 0L
)