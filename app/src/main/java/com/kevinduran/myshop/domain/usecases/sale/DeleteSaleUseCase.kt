package com.kevinduran.myshop.domain.usecases.sale

import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.domain.repositories.SalesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteSaleUseCase @Inject constructor(
    private val repository: SalesRepository
) {
    suspend operator fun invoke(entities: List<Sale>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                repository.deleteAll(entities)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
