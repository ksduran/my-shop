package com.kevinduran.myshop.domain.usecases.sale

import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.domain.repositories.SalesRepository
import javax.inject.Inject

class UpdateSaleUseCase @Inject constructor(
    private val repository: SalesRepository
) {
    suspend operator fun invoke(entity: Sale) {
        repository.update(entity)
    }
}
