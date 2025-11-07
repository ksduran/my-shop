package com.kevinduran.myshop.domain.usecases.supplierdata

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.config.enums.SupplierDataType
import com.kevinduran.myshop.domain.models.SupplierData
import com.kevinduran.myshop.domain.repositories.SupplierDataRepository
import javax.inject.Inject

class AddSupplierDataUseCase @Inject constructor(
    private val repository: SupplierDataRepository
) {
    suspend operator fun invoke(
        supplierId: String,
        productId: String,
        title: String,
        quantity: Int,
        image: String,
        purchasePrice: Int,
        createdAt: Long,
        type: String = SupplierDataType.INCOME.name,
    ) {
        val userName = Firebase.auth.currentUser?.displayName
            ?: Firebase.auth.currentUser?.email
            ?: ""
        val entity = SupplierData(
            supplierId = supplierId.trim(),
            productId = productId.trim(),
            title = title.trim(),
            quantity = quantity,
            purchasePrice = purchasePrice,
            type = type,
            image = image,
            raisedBy = userName,
            updatedBy = userName,
            updatedAt = createdAt,
            createdAt = createdAt,
        )
        repository.add(entity).getOrThrow()
    }
}