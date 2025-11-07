package com.kevinduran.myshop.domain.usecases.employee

import com.kevinduran.myshop.di.IoDispatcher
import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.domain.providers.CurrentUserProvider
import com.kevinduran.myshop.domain.providers.TimerProvider
import com.kevinduran.myshop.domain.repositories.EmployeesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToggleEmployeeAccessUseCase @Inject constructor(
    private val repository: EmployeesRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val currentUserProvider: CurrentUserProvider,
    private val timerProvider: TimerProvider
) {
    suspend operator fun invoke(entity: Employee): Result<Unit> {
        return withContext(dispatcher) {
            try {
                val now = timerProvider.getNowMillis()
                val currentUser = currentUserProvider.getNameOrEmail()
                val status = if (entity.status == 0) 1 else 0
                val updatedEntity = entity.copy(
                    status = status,
                    syncStatus = 0,
                    updatedBy = currentUser,
                    updatedAt = now
                )
                repository.update(updatedEntity)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
