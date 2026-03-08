package com.kevinduran.myshop.infrastructure.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val ref: String?,
    @ColumnInfo("sale_price") val salePrice: Int = 0,
    @ColumnInfo("purchase_price") val purchasePrice: Int,
    @ColumnInfo("supplier_name") val supplierName: String?,
    val synced: Int = 0,
    val deleted: Int = 0,
    @ColumnInfo("created_at") val createdAt: Long = 0L,
    @ColumnInfo("updated_at") val updatedAt: Long = 0L
)