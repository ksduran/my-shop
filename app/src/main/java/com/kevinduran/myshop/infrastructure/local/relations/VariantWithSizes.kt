package com.kevinduran.myshop.infrastructure.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.kevinduran.myshop.infrastructure.local.entities.ProductSizeEntity
import com.kevinduran.myshop.infrastructure.local.entities.ProductVariantEntity


data class VariantWithSizes(
    @Embedded
    val variant: ProductVariantEntity,

    @Relation(
        entity = ProductSizeEntity::class,
        parentColumn = "id",
        entityColumn = "variant_id"
    )
    val sizes: List<ProductSizeEntity>
)