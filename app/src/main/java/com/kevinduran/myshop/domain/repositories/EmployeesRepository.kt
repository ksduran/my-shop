package com.kevinduran.myshop.domain.repositories

import com.kevinduran.myshop.domain.models.Employee

interface EmployeesRepository {
    suspend fun add(employee: Employee): Result<Unit>
    suspend fun delete(employee: Employee): Result<Unit>
    suspend fun delete(employees: List<Employee>): Result<Unit>
    suspend fun get(): Result<List<Employee>>
    suspend fun update(employee: Employee): Result<Unit>

}