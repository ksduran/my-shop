package com.kevinduran.myshop.domain.models

import java.util.UUID

data class Supplier(
    val uuid: String = UUID.randomUUID().toString(),
    val name: String = "",
    val debtControl: Int = 0,
    val syncStatus: Int = 0,
    val deleted: Int = 0,
    val raisedBy: String = "",
    val updatedBy: String = "",
    val updatedAt: Long = 0L,
    val createdAt: Long = 0L
)
