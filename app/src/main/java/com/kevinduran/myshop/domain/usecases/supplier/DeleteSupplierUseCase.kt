package com.kevinduran.myshop.domain.usecases.supplier

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.domain.repositories.SupplierRepository
import java.time.Instant
import javax.inject.Inject

class DeleteSupplierUseCase @Inject constructor(
    private val repository: SupplierRepository
) {
    suspend operator fun invoke(entities: List<Supplier>) {
        val updatedSuppliers = entities.map { entity ->
            entity.copy(
                deleted = 1,
                syncStatus = 0,
                updatedBy = Firebase.auth.currentUser?.displayName
                    ?: Firebase.auth.currentUser?.email
                    ?: "",
                updatedAt = Instant.now().toEpochMilli(),
            )
        }
        repository.delete(updatedSuppliers).getOrThrow()
    }
}