package com.kevinduran.myshop.domain.usecases.employee

import com.kevinduran.myshop.di.IoDispatcher
import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.domain.repositories.EmployeesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteEmployeesUseCase @Inject constructor(
    private val repository: EmployeesRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(entities: List<Employee>): Result<Unit> {

        return withContext(ioDispatcher) {
            try {
                if (entities.isEmpty()) Result.success(Unit)
                repository.delete(entities)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
