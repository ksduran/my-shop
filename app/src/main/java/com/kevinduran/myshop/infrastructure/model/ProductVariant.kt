package com.kevinduran.myshop.infrastructure.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kevinduran.myshop.infrastructure.local.entities.ProductEntity

@Entity(
    tableName = "product_variants",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("product_id")]
)
data class ProductVariant(
    @PrimaryKey
    val id: String,
    @ColumnInfo("product_id") val productId: String,
    val color: String,
    @ColumnInfo("image_url") val imageUrl: String
)