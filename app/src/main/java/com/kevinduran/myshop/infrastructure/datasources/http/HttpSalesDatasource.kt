package com.kevinduran.myshop.infrastructure.datasources.http

import android.content.Context
import com.kevinduran.myshop.domain.datasources.SalesDatasource
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.infrastructure.mappers.toRequest
import com.kevinduran.myshop.infrastructure.model.EarningsSummary
import com.kevinduran.myshop.infrastructure.model.StatisticsByDateRange
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import com.kevinduran.myshop.infrastructure.services.retrofit.SalesService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

class HttpSalesDatasource @Inject constructor(
    private val service: SalesService,
    private val preferences: UserPreferencesRepository,
    @ApplicationContext private val context: Context
) : SalesDatasource {
    override suspend fun add(entity: Sale): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entity.toRequest(access.first)
                val response = service.putBatch(
                    license = access.first,
                    packageName = access.second,
                    request = listOf(request)
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al subir la venta. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun addAll(sales: List<Sale>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = sales.map { it.toRequest(access.first) }
                val response = service.putBatch(access.first, access.second, request)
                if (!response.isSuccessful) {
                    throw Exception("Error al subir las ventas. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun delete(entity: Sale): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entity.toRequest(access.first)
                val response = service.deleteBatch(
                    license = access.first,
                    packageName = access.second,
                    request = listOf(request)
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al eliminar la venta. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun deleteAll(entities: List<Sale>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entities.map { it.toRequest(access.first) }
                val response = service.deleteBatch(
                    license = access.first,
                    packageName = access.second,
                    request = request
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al eliminar las ventas. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getToday(
        companyName: String,
        paymentStatus: String,
        raisedBy: String,
        from: Long,
        to: Long
    ): Result<List<Sale>> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val response = service.getToday(
                    license = access.first,
                    packageName = access.second,
                    companyName = companyName,
                    paymentStatus = paymentStatus,
                    raisedBy = raisedBy
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al obtener las ventas del dia. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(response.body() ?: emptyList())
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getEarningsSummary(start: Long, end: Long): Result<EarningsSummary> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val response = service.getEarningsSummary(
                    license = access.first,
                    packageName = access.second,
                    from = start,
                    to = end
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al obtener el resumen de ganancias. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(response.body() ?: EarningsSummary(0, 0))
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getByDateRange(
        companyName: String,
        paymentStatus: String,
        raisedBy: String,
        from: Long,
        to: Long
    ): Result<List<Sale>> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val response = service.getByRange(
                    license = access.first,
                    packageName = access.second,
                    companyName = companyName,
                    paymentStatus = paymentStatus,
                    raisedBy = raisedBy,
                    from = from,
                    to = to
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al obtener las ventas del dia. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(response.body() ?: emptyList())
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getBySupplier(
        supplierId: String,
        from: Long,
        to: Long
    ): Result<List<Sale>> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val response = service.getBySupplier(
                    license = access.first,
                    packageName = access.second,
                    supplierId = supplierId,
                    from = from,
                    to = to
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al obtener las ventas del dia. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(response.body() ?: emptyList())
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getStatisticsByDateRange(
        productId: String,
        from: Long,
        to: Long
    ): Result<StatisticsByDateRange> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val response = service.getStatistics(
                    license = access.first,
                    packageName = access.second,
                    productId = productId,
                    from = from,
                    to = to
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al obtener las estadisticas de venta del producto. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(response.body() ?: StatisticsByDateRange())
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun update(entity: Sale): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entity.toRequest(access.first)
                val response = service.putBatch(access.first, access.second, listOf(request))
                if (!response.isSuccessful) {
                    throw Exception("Error al actualizar la venta. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun updatedAll(entities: List<Sale>): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val request = entities.map { it.toRequest(access.first) }
                val response = service.putBatch(access.first, access.second, request)
                if (!response.isSuccessful) {
                    throw Exception("Error al actualizar las ventas. Code: ${response.code()}, ${response.message()}")
                }
                Result.success(Unit)
            } catch (_: IOException) {
                Result.failure(Exception("Error de conexión, verifique su internet e intente de nuevo"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getTotalCashByUser(raisedBy: String): Result<Int> {
        return withContext(Dispatchers.IO) {
            try {
                val access = httpAccess()
                val response = service.getTotalCashByRaised(
                    license = access.first,
                    packageName = access.second,
                    raisedBy = raisedBy
                )
                if (!response.isSuccessful) {
                    throw Exception("Error al obtener el total racaudado para el usuario \"$raisedBy\". Code: ${response.code()}, ${response.message()}")
                }
                Result.success(response.body() ?: 0)
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