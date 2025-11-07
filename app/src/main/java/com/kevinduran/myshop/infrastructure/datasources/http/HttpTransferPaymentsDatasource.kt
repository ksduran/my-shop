package com.kevinduran.myshop.infrastructure.datasources.http

import android.content.Context
import com.kevinduran.myshop.domain.datasources.TransferPaymentsDatasource
import com.kevinduran.myshop.domain.models.TransferPayment
import com.kevinduran.myshop.infrastructure.mappers.toModel
import com.kevinduran.myshop.infrastructure.mappers.toRequest
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import com.kevinduran.myshop.infrastructure.services.retrofit.TransferPaymentsService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

class HttpTransferPaymentsDatasource @Inject constructor(
    private val service: TransferPaymentsService,
    private val preferences: UserPreferencesRepository,
    @ApplicationContext private val context: Context
) : TransferPaymentsDatasource {
    override suspend fun add(entity: TransferPayment): Result<Unit> {
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
                    throw Exception("Error al subir el pago. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun addAll(transferPayments: List<TransferPayment>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = transferPayments.map { it.toRequest(access.first) }
                val response = service.upsertAll(
                    license = access.first,
                    packageName = access.second,
                    products = request
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al subir los pagos. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun delete(entity: TransferPayment): Result<Unit> {
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
                    throw Exception("Error al eliminar el pago. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun delete(payments: List<TransferPayment>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = payments.map { it.toRequest(access.first) }
                val response = service.deleteAll(
                    license = access.first,
                    packageName = access.second,
                    products = request
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al eliminar los pagos. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getAll(): Result<List<TransferPayment>> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val response = service.getAll(
                    license = access.first,
                    packageName = access.second
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al obtener los pagos. Code: ${response.code()}, ${response.message()}")
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

    override suspend fun update(entity: TransferPayment): Result<Unit> {
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
                    throw Exception("Error al actualizar el pago. Code: ${response.code()}, ${response.message()}")
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