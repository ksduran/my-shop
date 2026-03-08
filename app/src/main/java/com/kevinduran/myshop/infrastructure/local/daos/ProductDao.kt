package com.kevinduran.myshop.infrastructure.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kevinduran.myshop.infrastructure.local.entities.ProductEntity
import com.kevinduran.myshop.infrastructure.local.entities.ProductSizeEntity
import com.kevinduran.myshop.infrastructure.local.entities.ProductVariantEntity
import com.kevinduran.myshop.infrastructure.local.relations.ProductWithVariants

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariants(variants: List<ProductVariantEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSizes(sizes: List<ProductSizeEntity>)

    @Transaction
    suspend fun insertFullProduct(
        product: ProductEntity,
        variants: List<ProductVariantEntity>,
        sizes: List<ProductSizeEntity>
    ) {
        insertProduct(product)
        insertVariants(variants)
        insertSizes(sizes)
    }

    @Transaction
    @Query("""
        SELECT *
        FROM products
        WHERE id = :productId AND deleted = 0
    """)
    suspend fun getProduct(productId: String): ProductWithVariants?

    @Transaction
    @Query("""
        SELECT *
        FROM products
        WHERE deleted = 0
        ORDER BY created_at DESC
    """)
    suspend fun getProducts(): List<ProductWithVariants>


}