package com.kevinduran.myshop.domain.usecases.sale

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.config.constants.Payment
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.domain.models.SaleReturn
import com.kevinduran.myshop.domain.models.SaleReturnProduct
import com.kevinduran.myshop.domain.repositories.SaleReturnsRepository
import com.kevinduran.myshop.domain.repositories.SalesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class RegisterSaleReturnUseCase @Inject constructor(
    private val saleReturnsRepository: SaleReturnsRepository,
    private val salesRepository: SalesRepository
) {
    suspend operator fun invoke(
        storeName: String,
        imageFileName: String,
        products: List<SaleReturnProduct>,
        updatedSales: List<Sale>
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val now = Instant.now().toEpochMilli()
                val saleReturn = SaleReturn(
                    uuid = UUID.randomUUID().toString(),
                    storeName = storeName,
                    imagePath = imageFileName,
                    products = products,
                    updatedAt = now,
                    createdAt = now
                )
                saleReturnsRepository.add(saleReturn)
                val allSales = updatedSales.map { entity ->
                    entity.copy(
                        syncStatus = 0,
                        updatedBy = Firebase.auth.currentUser?.displayName
                            ?: Firebase.auth.currentUser?.email
                            ?: "",
                        updatedAt = now,
                        paymentStatus = Payment.Return.status,
                        changedProductId = "",
                        changedSize = "",
                        changedProductColor = ""
                    )
                }
                salesRepository.updatedAll(allSales)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}