package com.kevinduran.myshop.infrastructure.repositories

import com.kevinduran.myshop.domain.datasources.EmployeesDatasource
import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.domain.repositories.EmployeesRepository
import javax.inject.Inject

class EmployeesRepositoryImpl @Inject constructor(
    private val datasource: EmployeesDatasource
) : EmployeesRepository {
    override suspend fun add(employee: Employee) = datasource.add(employee)
    override suspend fun delete(employee: Employee) = datasource.delete(employee)
    override suspend fun delete(employees: List<Employee>) = datasource.delete(employees)
    override suspend fun get(): Result<List<Employee>> = datasource.get()
    override suspend fun update(employee: Employee) = datasource.update(employee)
}