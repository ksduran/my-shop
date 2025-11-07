package com.kevinduran.myshop.domain.usecases.supplierdata

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.domain.models.SupplierData
import com.kevinduran.myshop.domain.repositories.SupplierDataRepository
import java.time.Instant
import javax.inject.Inject

class DeleteSupplierDataUseCase @Inject constructor(
    private val repository: SupplierDataRepository
) {
    suspend operator fun invoke(entities: List<SupplierData>) {
        val updatedEntities = entities.map { entity ->
            entity.copy(
                deleted = 1,
                syncStatus = 0,
                updatedBy = Firebase.auth.currentUser?.displayName
                    ?: Firebase.auth.currentUser?.email
                    ?: "",
                updatedAt = Instant.now().toEpochMilli(),
            )
        }
        repository.delete(updatedEntities).getOrThrow()
    }
}