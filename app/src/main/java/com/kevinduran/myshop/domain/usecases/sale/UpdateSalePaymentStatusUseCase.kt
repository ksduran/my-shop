package com.kevinduran.myshop.domain.usecases.sale

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.config.constants.Payment
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.domain.repositories.SalesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UpdateSalePaymentStatusUseCase @Inject constructor(
    private val repository: SalesRepository
) {
    suspend operator fun invoke(entities: List<Sale>, paymentStatus: Payment): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val allEntities: List<Sale> = entities.map { entity ->
                    var updatedEntity = entity.copy(
                        syncStatus = 0,
                        updatedBy = Firebase.auth.currentUser?.displayName
                            ?: Firebase.auth.currentUser?.email
                            ?: "",
                        updatedAt = System.currentTimeMillis(),
                        paymentStatus = paymentStatus.status
                    )
                    if (paymentStatus != Payment.Change) {
                        updatedEntity = updatedEntity.copy(
                            changedProductId = "",
                            changedSize = "",
                            changedProductColor = ""
                        )
                    }
                    updatedEntity
                }
                repository.updatedAll(allEntities)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}