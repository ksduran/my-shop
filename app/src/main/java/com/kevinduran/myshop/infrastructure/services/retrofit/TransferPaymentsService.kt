package com.kevinduran.myshop.infrastructure.services.retrofit

import com.kevinduran.myshop.infrastructure.request.TransferPaymentRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TransferPaymentsService {
    @GET("transfer-payments")
    suspend fun getAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String
    ): Response<List<TransferPaymentRequest>>

    @POST("transfer-payments/bulkDelete")
    suspend fun deleteAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Body products: List<TransferPaymentRequest>
    ): Response<Unit>

    @POST("transfer-payments")
    suspend fun upsertAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Body products: List<TransferPaymentRequest>
    ): Response<Unit>
}