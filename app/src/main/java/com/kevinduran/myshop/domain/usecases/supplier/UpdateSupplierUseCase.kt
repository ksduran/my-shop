package com.kevinduran.myshop.domain.usecases.supplier

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.domain.repositories.SupplierRepository
import java.time.Instant
import javax.inject.Inject

class UpdateSupplierUseCase @Inject constructor(
    private val repository: SupplierRepository
) {
    suspend operator fun invoke(name: String, debtControl: Int, entity: Supplier) {
        val trimmedName = name.trim()
        require(trimmedName.isNotBlank()) { "El nombre del proveedor es obligatorio" }

        val suppliers = repository.get().getOrThrow()
        val exists = suppliers.any { it.uuid != entity.uuid && it.name.equals(trimmedName, ignoreCase = true) }
        if (exists) throw IllegalArgumentException("Ya existe un proveedor con el mismo nombre")

        val updatedSupplier = entity.copy(
            name = trimmedName,
            debtControl = debtControl,
            syncStatus = 0,
            updatedBy = Firebase.auth.currentUser?.displayName
                ?: Firebase.auth.currentUser?.email
                ?: "",
            updatedAt = Instant.now().toEpochMilli(),
        )
        repository.update(updatedSupplier).getOrThrow()
    }
}