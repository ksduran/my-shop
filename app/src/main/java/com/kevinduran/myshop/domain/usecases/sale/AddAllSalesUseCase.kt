package com.kevinduran.myshop.domain.usecases.sale

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.config.constants.Payment
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.domain.repositories.SalesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddAllSalesUseCase @Inject constructor(
    private val repository: SalesRepository
) {
    suspend operator fun invoke(
        companyName: String,
        color: String,
        size: String,
        sizeR: String,
        salePrice: Int,
        image: String,
        products: List<Product>
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val userName = Firebase.auth.currentUser?.displayName
                    ?: Firebase.auth.currentUser?.email
                    ?: ""
                val now = System.currentTimeMillis()
                val allSales = products.map { product ->
                    Sale(
                        productId = product.uuid,
                        companyName = companyName,
                        supplierId = product.supplierName,
                        color = color,
                        size = size,
                        sizeR = sizeR,
                        salePrice = salePrice,
                        purchasePrice = product.purchasePrice,
                        paymentStatus = Payment.ByRaised.status,
                        image = image,
                        raisedBy = "@Administrador",
                        updatedBy = userName,
                        updatedAt = now,
                        createdAt = now,
                    )
                }
                repository.addAll(allSales)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
