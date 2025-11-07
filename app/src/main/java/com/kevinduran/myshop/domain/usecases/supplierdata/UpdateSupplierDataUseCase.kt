package com.kevinduran.myshop.domain.usecases.supplierdata

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.domain.models.SupplierData
import com.kevinduran.myshop.domain.repositories.SupplierDataRepository
import java.time.Instant
import javax.inject.Inject

class UpdateSupplierDataUseCase @Inject constructor(
    private val repository: SupplierDataRepository
) {
    suspend operator fun invoke(
        title: String,
        quantity: Int,
        purchasePrice: Int,
        entity: SupplierData,
    ) {
        val updatedEntity = entity.copy(
            title = title,
            quantity = quantity,
            purchasePrice = purchasePrice,
            syncStatus = 0,
            updatedBy = Firebase.auth.currentUser?.displayName
                ?: Firebase.auth.currentUser?.email
                ?: "",
            updatedAt = Instant.now().toEpochMilli(),
        )
        repository.update(updatedEntity).getOrThrow()
    }
}