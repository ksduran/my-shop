package com.kevinduran.myshop.infrastructure.datasources.http

import android.content.Context
import com.kevinduran.myshop.domain.datasources.SaleReturnsDatasource
import com.kevinduran.myshop.domain.models.SaleReturn
import com.kevinduran.myshop.infrastructure.mappers.toModel
import com.kevinduran.myshop.infrastructure.mappers.toRequest
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import com.kevinduran.myshop.infrastructure.services.retrofit.SaleReturnsService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

class HttpSaleReturnsDatasource @Inject constructor(
    private val service: SaleReturnsService,
    private val preferences: UserPreferencesRepository,
    @ApplicationContext private val context: Context
) : SaleReturnsDatasource {

    override suspend fun add(entity: SaleReturn): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entity.toRequest(access.first)
                val response = service.upsertAll(
                    license = access.first,
                    packageName = access.second,
                    products = listOf(request)
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al subir la devolución. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun add(returns: List<SaleReturn>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = returns.map { it.toRequest(access.first) }
                val response = service.upsertAll(
                    license = access.first,
                    packageName = access.second,
                    products = request
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al subir la devolución. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun delete(entity: SaleReturn): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entity.toRequest(access.first)
                val response = service.deleteAll(
                    license = access.first,
                    packageName = access.second,
                    products = listOf(request)
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al eliminar la devolución. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun delete(returns: List<SaleReturn>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = returns.map { it.toRequest(access.first) }
                val response = service.deleteAll(
                    license = access.first,
                    packageName = access.second,
                    products = request
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al eliminar la devolución. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getAll(): Result<List<SaleReturn>> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val response = service.getAll(
                    license = access.first,
                    packageName = access.second
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al obtener las devoluciones. Code: ${response.code()}, ${response.message()}")
                }
                val list = response.body()?.map { it.toModel() }
                Result.success(list ?: emptyList())
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun update(entity: SaleReturn): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entity.toRequest(access.first)
                val response = service.upsertAll(access.first, access.second, listOf(request))
                if (!response.isSuccessful) {
                    throw Exception("Error al actualizar la devolución. Code: ${response.code()}, ${response.message()}")
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