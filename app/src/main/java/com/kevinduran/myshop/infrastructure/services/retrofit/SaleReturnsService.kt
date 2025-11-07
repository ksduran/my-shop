package com.kevinduran.myshop.infrastructure.services.retrofit

import com.kevinduran.myshop.infrastructure.request.SaleReturnRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SaleReturnsService {
    @GET("sale-returns")
    suspend fun getAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String
    ): Response<List<SaleReturnRequest>>

    @POST("sale-returns/bulkDelete")
    suspend fun deleteAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Body products: List<SaleReturnRequest>
    ): Response<Unit>

    @POST("sale-returns")
    suspend fun upsertAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Body products: List<SaleReturnRequest>
    ): Response<Unit>

}