package com.kevinduran.myshop.domain.usecases.sale

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.domain.repositories.SalesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AssignUserToSalesUseCase @Inject constructor(
    private val repository: SalesRepository
) {

    suspend operator fun invoke(entities: List<Sale>, user: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val list = entities.map { sale ->
                    sale.copy(
                        raisedBy = user,
                        updatedAt = System.currentTimeMillis(),
                        updatedBy = Firebase.auth.currentUser?.displayName ?: "Unknown"
                    )
                }
                repository.updatedAll(list)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

}