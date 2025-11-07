package com.kevinduran.myshop.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class TransferPaymentProduct(
    val productId: String,
    val size: String,
    val image: String
)