package com.kevinduran.myshop.infrastructure.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_sizes",
    foreignKeys = [
        ForeignKey(
            entity = ProductVariantEntity::class,
            parentColumns = ["id"],
            childColumns = ["variant_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("variant_id")]
)
data class ProductSizeEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo("variant_id") val variantId: String,
    val size: String,
    val stock: Int
)