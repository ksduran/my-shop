package com.kevinduran.myshop.infrastructure.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.kevinduran.myshop.infrastructure.local.entities.ProductEntity
import com.kevinduran.myshop.infrastructure.local.entities.ProductVariantEntity

data class ProductWithVariants(
    @Embedded
    val product: ProductEntity,

    @Relation(
        entity = ProductVariantEntity::class,
        parentColumn = "id",
        entityColumn = "product_id"
    )
    val variants: List<VariantWithSizes>
)