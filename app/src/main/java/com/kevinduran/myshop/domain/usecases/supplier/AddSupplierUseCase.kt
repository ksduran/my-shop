package com.kevinduran.myshop.domain.usecases.supplier

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.domain.repositories.SupplierRepository
import java.time.Instant
import javax.inject.Inject

class AddSupplierUseCase @Inject constructor(
    private val repository: SupplierRepository
) {
    suspend operator fun invoke(name: String, debtControl: Int): Supplier {
        val trimmedName = name.trim()
        require(trimmedName.isNotBlank()) { "El nombre del proveedor es obligatorio" }

        val suppliers = repository.get().getOrThrow()
        val exists = suppliers.any { it.name.equals(trimmedName, ignoreCase = true) }
        if (exists) throw IllegalArgumentException("Ya existe un proveedor con el mismo nombre")

        val userName = Firebase.auth.currentUser?.displayName
            ?: Firebase.auth.currentUser?.email
            ?: ""

        val entity = Supplier(
            name = trimmedName,
            debtControl = debtControl,
            raisedBy = userName,
            updatedBy = userName,
            updatedAt = Instant.now().toEpochMilli(),
            createdAt = Instant.now().toEpochMilli(),
        )

        repository.add(entity).getOrThrow()
        return entity
    }
}