package com.kevinduran.myshop.infrastructure.datasources.http

import android.content.Context
import com.kevinduran.myshop.domain.datasources.SupplierDataDatasource
import com.kevinduran.myshop.domain.models.SupplierData
import com.kevinduran.myshop.infrastructure.mappers.toRequest
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import com.kevinduran.myshop.infrastructure.services.retrofit.SupplierDataService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

class HttpSupplierDataDatasource @Inject constructor(
    private val service: SupplierDataService,
    private val preferences: UserPreferencesRepository,
    @ApplicationContext private val context: Context
) : SupplierDataDatasource {
    override suspend fun add(entity: SupplierData): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entity.toRequest(access.first)
                val response = service.upsertAll(access.first, access.second, listOf(request))
                if (!response.isSuccessful) {
                    throw Exception("Error adding supplier data: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun add(supplierData: List<SupplierData>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = supplierData.map { it.toRequest(access.first) }
                val response = service.upsertAll(access.first, access.second, request)
                if (!response.isSuccessful) {
                    throw Exception("Error adding supplier data: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun delete(entity: SupplierData): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entity.toRequest(access.first)
                val response = service.deleteAll(access.first, access.second, listOf(request))
                if (!response.isSuccessful) {
                    throw Exception("Error deleting supplier data: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun delete(supplierData: List<SupplierData>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = supplierData.map { it.toRequest(access.first) }
                val response = service.deleteAll(access.first, access.second, request)
                if (!response.isSuccessful) {
                    throw Exception("Error deleting supplier data: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun get(supplierId: String): Result<List<SupplierData>> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val data = service.getAll(access.first, access.second)
                    .filter { it.supplierId == supplierId }
                Result.success(data)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun update(entity: SupplierData): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entity.toRequest(access.first)
                val response = service.upsertAll(access.first, access.second, listOf(request))
                if (!response.isSuccessful) {
                    throw Exception("Error updating supplier data: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun update(supplierData: List<SupplierData>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = supplierData.map { it.toRequest(access.first) }
                val response = service.upsertAll(access.first, access.second, request)
                if (!response.isSuccessful) {
                    throw Exception("Error updating supplier data: ${response.code()}, ${response.message()}")
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