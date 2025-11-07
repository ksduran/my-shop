package com.kevinduran.myshop.infrastructure.datasources.http

import android.content.Context
import com.kevinduran.myshop.domain.datasources.SuppliersDatasource
import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.infrastructure.mappers.toRequest
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import com.kevinduran.myshop.infrastructure.services.retrofit.SuppliersService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

class HttpSupplierDatasource @Inject constructor(
    private val service: SuppliersService,
    private val preferences: UserPreferencesRepository,
    @ApplicationContext private val context: Context
) : SuppliersDatasource {
    override suspend fun add(entity: Supplier): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entity.toRequest(access.first)
                val response = service.upsertAll(access.first, access.second, listOf(request))
                if (!response.isSuccessful) {
                    throw Exception("Error adding supplier: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun add(suppliers: List<Supplier>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = suppliers.map { it.toRequest(access.first) }
                val response = service.upsertAll(access.first, access.second, request)
                if (!response.isSuccessful) {
                    throw Exception("Error adding suppliers: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun delete(entity: Supplier): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entity.toRequest(access.first)
                val response = service.deleteAll(access.first, access.second, listOf(request))
                if (!response.isSuccessful) {
                    throw Exception("Error deleting supplier: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun delete(suppliers: List<Supplier>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = suppliers.map { it.toRequest(access.first) }
                val response = service.deleteAll(access.first, access.second, request)
                if (!response.isSuccessful) {
                    throw Exception("Error deleting suppliers: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun get(): Result<List<Supplier>> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val suppliers = service.getAll(access.first, access.second)
                Result.success(suppliers)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun update(entity: Supplier): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entity.toRequest(access.first)
                val response = service.upsertAll(access.first, access.second, listOf(request))
                if (!response.isSuccessful) {
                    throw Exception("Error updating supplier: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun update(suppliers: List<Supplier>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = suppliers.map { it.toRequest(access.first) }
                val response = service.upsertAll(access.first, access.second, request)
                if (!response.isSuccessful) {
                    throw Exception("Error updating suppliers: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private suspend fun httpAccess(): Pair<String, String> {
        val license = preferences.getLicenseId().first().toString()
        val packageName = context.packageName
        return Pair(license, packageName)
    }
}