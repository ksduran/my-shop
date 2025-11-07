package com.kevinduran.myshop.domain.usecases.product

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.domain.repositories.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(
        entity: Product,
        name: String,
        ref: String,
        salePrice: Int,
        purchasePrice: Int,
        supplierName: String,
        variants: String,
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val userName = Firebase.auth.currentUser?.displayName
                    ?: Firebase.auth.currentUser?.email
                    ?: ""
                val updatedEntity = entity.copy(
                    name = name,
                    ref = ref,
                    salePrice = salePrice,
                    purchasePrice = purchasePrice,
                    supplierName = supplierName,
                    variants = variants,
                    syncStatus = 0,
                    imageSyncStatus = 0,
                    updatedBy = userName,
                    updatedAt = Instant.now().toEpochMilli(),
                )
                repository.update(updatedEntity)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
