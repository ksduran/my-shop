package com.kevinduran.myshop.infrastructure.datasources.http

import android.content.Context
import com.kevinduran.myshop.domain.datasources.EmployeesDatasource
import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.infrastructure.mappers.toRequest
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import com.kevinduran.myshop.infrastructure.services.retrofit.EmployeesService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import okio.IOException
import javax.inject.Inject

class HttpEmployeeDatasource @Inject constructor(
    private val service: EmployeesService,
    private val preferences: UserPreferencesRepository,
    @ApplicationContext private val context: Context
) : EmployeesDatasource {

    override suspend fun add(employee: Employee): Result<Unit> {
        return try {
            val access = httpAccess()
            val request = employee.toRequest(access.first)
            val response = service.upsertAll(access.first, access.second, listOf(request))
            if (!response.isSuccessful) {
                throw Exception("Error al subir el registro. Code: ${response.code()}, ${response.message()}")
            }
            Result.success(Unit)
        } catch (_: IOException) {
            Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(employee: Employee): Result<Unit> {
        return try {
            val access = httpAccess()
            val request = employee.toRequest(access.first)
            val response = service.deleteAll(access.first, access.second, listOf(request))
            if (!response.isSuccessful) {
                throw Exception("Error al borrar el registro. Code: ${response.code()}, ${response.message()}")
            }
            Result.success(Unit)
        } catch (_: IOException) {
            Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(employees: List<Employee>): Result<Unit> {
        return try {
            val access = httpAccess()
            val request = employees.map { it.toRequest(access.second) }
            val response = service.deleteAll(access.first, access.second, request)
            if (!response.isSuccessful) {
                throw Exception("Error al borrar el registro. Code: ${response.code()}, ${response.message()}")
            }
            Result.success(Unit)
        } catch (_: IOException) {
            Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun get(): Result<List<Employee>> {
        return try {
            val access = httpAccess()
            val response = service.getAll(access.first, access.second)
            if (!response.isSuccessful) {
                throw Exception("Error al obtener los empleados. Code: ${response.code()}, ${response.message()}")
            }
            Result.success(response.body()!!)
        } catch (_: IOException) {
            Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun update(employee: Employee): Result<Unit> {
        return try {
            val access = httpAccess()
            val request = employee.toRequest(access.first)
            val response = service.upsertAll(access.first, access.second, listOf(request))
            if (!response.isSuccessful) {
                throw Exception("Error al actualizar el registro. Code: ${response.code()}, ${response.message()}")
            }
            Result.success(Unit)
        } catch (_: IOException) {
            Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun httpAccess(): Pair<String, String> {
        val license = preferences.getLicenseId().first().toString()
        val packageName = context.packageName
        return Pair(license, packageName)
    }
}