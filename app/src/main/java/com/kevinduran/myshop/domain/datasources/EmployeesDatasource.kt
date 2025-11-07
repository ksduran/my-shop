package com.kevinduran.myshop.domain.datasources

import com.kevinduran.myshop.domain.models.Employee

interface EmployeesDatasource {
    suspend fun add(employee: Employee): Result<Unit>
    suspend fun delete(employee: Employee): Result<Unit>
    suspend fun delete(employees: List<Employee>): Result<Unit>
    suspend fun get(): Result<List<Employee>>
    suspend fun update(employee: Employee): Result<Unit>
}
