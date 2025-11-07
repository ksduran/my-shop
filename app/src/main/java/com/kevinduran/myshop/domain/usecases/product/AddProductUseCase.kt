package com.kevinduran.myshop.domain.usecases.product

import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.domain.providers.CurrentUserProvider
import com.kevinduran.myshop.domain.providers.TimerProvider
import com.kevinduran.myshop.domain.repositories.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddProductUseCase @Inject constructor(
    private val repository: ProductsRepository,
    private val currentUserProvider: CurrentUserProvider,
    private val timerProvider: TimerProvider,
) {
    suspend operator fun invoke(
        uuid: String,
        name: String,
        ref: String,
        salePrice: Int,
        purchasePrice: Int,
        supplierName: String,
        variants: String,
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val userName = currentUserProvider.getNameOrEmail()
                val now = timerProvider.getNowMillis()
                val entity = Product(
                    uuid = uuid,
                    name = name.trim(),
                    ref = ref.trim(),
                    salePrice = salePrice,
                    purchasePrice = purchasePrice,
                    supplierName = supplierName.trim(),
                    variants = variants,
                    raisedBy = userName,
                    updatedBy = userName,
                    updatedAt = now,
                    createdAt = now,
                )
                repository.add(entity)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
