package com.kevinduran.myshop.infrastructure.datasources.http

import android.content.Context
import com.kevinduran.myshop.domain.datasources.ProductsDatasource
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.infrastructure.mappers.toRequest
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import com.kevinduran.myshop.infrastructure.services.retrofit.ProductsService
import com.kevinduran.myshop.infrastructure.services.retrofit.StorageService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

class HttpProductsDatasource @Inject constructor(
    private val productService: ProductsService,
    private val storageService: StorageService,
    private val preferences: UserPreferencesRepository,
    @ApplicationContext private val context: Context
) : ProductsDatasource {

    override suspend fun add(entity: Product): Result<Unit> {
        return try {
            val access = httpAccess()
            val request = entity.toRequest(access.first)
            val response = productService.upsertAll(access.first, access.second, listOf(request))
            if (!response.isSuccessful) {
                throw Exception("Error al subir el producto. Code: ${response.code()}, ${response.message()}")
            }
            Result.success(Unit)
        } catch (_: IOException) {
            Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addAll(products: List<Product>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = products.map { it.toRequest(access.first) }
                val response = productService.upsertAll(access.first, access.second, request)
                if (!response.isSuccessful) {
                    throw Exception("Error al subir lost productos. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun delete(entity: Product): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entity.toRequest(access.first)
                val response =
                    productService.deleteAll(access.first, access.second, listOf(request))
                if (!response.isSuccessful) {
                    throw Exception("Error al eliminar el producto. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun delete(products: List<Product>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = products.map { it.toRequest(access.first) }
                val response =
                    productService.deleteAll(access.first, access.second, request)
                if (!response.isSuccessful) {
                    throw Exception("Error al eliminar el producto. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun get(): Result<List<Product>> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val response = productService.getAll(access.first, access.second)
                if (!response.isSuccessful) {
                    throw Exception("Error al obtener los productos. Code: ${response.code()}, ${response.message()}")
                }
                val list = response.body() ?: emptyList()
                Result.success(list)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getByUuid(uuid: String): Result<Product?> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val response = productService.getById(
                    license = access.first,
                    packageName = access.second,
                    id = uuid
                )
                if (!response.isSuccessful) {
                    return@withContext Result.success(null)
                }
                Result.success(response.body())
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun update(entity: Product): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entity.toRequest(access.first)
                val response =
                    productService.upsertAll(access.first, access.second, listOf(request))
                if (!response.isSuccessful) {
                    throw Exception("Error al actualizar el producto. Code: ${response.code()}, ${response.message()}")
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