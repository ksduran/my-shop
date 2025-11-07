package com.kevinduran.myshop.domain.usecases.employee

import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.domain.repositories.EmployeesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAllEmployeesUseCase @Inject constructor(
    private val repository: EmployeesRepository
) {
    suspend operator fun invoke(): Result<List<Employee>> {
        return withContext(Dispatchers.IO) {
            repository.get()
        }
    }
}