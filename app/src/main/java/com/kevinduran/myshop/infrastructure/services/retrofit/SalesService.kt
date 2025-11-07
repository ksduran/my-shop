package com.kevinduran.myshop.infrastructure.services.retrofit

import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.infrastructure.model.EarningsSummary
import com.kevinduran.myshop.infrastructure.model.StatisticsByDateRange
import com.kevinduran.myshop.infrastructure.request.SaleRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SalesService {
    @GET("sales")
    suspend fun getByRange(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Query("companyName") companyName: String,
        @Query("paymentStatus") paymentStatus: String,
        @Query("raisedBy") raisedBy: String,
        @Query("from") from: Long,
        @Query("to") to: Long
    ): Response<List<Sale>>

    @GET("sales/today")
    suspend fun getToday(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Query("companyName") companyName: String,
        @Query("paymentStatus") paymentStatus: String,
        @Query("raisedBy") raisedBy: String
    ): Response<List<Sale>>

    @GET("sales/supplier")
    suspend fun getBySupplier(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Query("supplierId") supplierId: String,
        @Query("from") from: Long,
        @Query("to") to: Long
    ): Response<List<Sale>>

    @GET("sales/statistics")
    suspend fun getStatistics(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Query("productId") productId: String,
        @Query("from") from: Long,
        @Query("to") to: Long
    ): Response<StatisticsByDateRange>

    @GET("sales/earnings-summary")
    suspend fun getEarningsSummary(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Query("from") from: Long,
        @Query("to") to: Long
    ): Response<EarningsSummary>

    @GET("sales/total-cash")
    suspend fun getTotalCashByRaised(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Query("raisedBy") raisedBy: String
    ): Response<Int>

    @POST("sales")
    suspend fun putBatch(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Body request: List<SaleRequest>
    ): Response<Unit>

    @POST("sales/bulkDelete")
    suspend fun deleteBatch(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Body request: List<SaleRequest>
    ): Response<Unit>
}