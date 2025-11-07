package com.kevinduran.myshop.domain.usecases.sale

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.config.constants.Payment
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.domain.models.TransferPayment
import com.kevinduran.myshop.domain.models.TransferPaymentProduct
import com.kevinduran.myshop.domain.repositories.SalesRepository
import com.kevinduran.myshop.domain.repositories.TransferPaymentsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class RegisterTransferPaymentUseCase @Inject constructor(
    private val transferPaymentsRepository: TransferPaymentsRepository,
    private val salesRepository: SalesRepository
) {
    suspend operator fun invoke(
        storeName: String,
        imageFileName: String,
        products: List<TransferPaymentProduct>,
        updatedSales: List<Sale>
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val now = Instant.now().toEpochMilli()
                val transferPayment = TransferPayment(
                    uuid = UUID.randomUUID().toString(),
                    storeName = storeName,
                    imagePath = imageFileName,
                    products = products,
                    updatedAt = now,
                    createdAt = now
                )
                transferPaymentsRepository.add(transferPayment)
                val batch = updatedSales.map { entity ->
                    entity.copy(
                        syncStatus = 0,
                        updatedBy = Firebase.auth.currentUser?.displayName
                            ?: Firebase.auth.currentUser?.email
                            ?: "",
                        updatedAt = now,
                        paymentStatus = Payment.PaidByTransfer.status,
                        changedProductId = "",
                        changedSize = "",
                        changedProductColor = ""
                    )
                }
                salesRepository.updatedAll(batch)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}