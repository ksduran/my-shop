package com.kevinduran.myshop.infrastructure.services.retrofit

import com.kevinduran.myshop.domain.models.SupplierData
import com.kevinduran.myshop.infrastructure.request.SupplierDataRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SupplierDataService {
    @GET("suppliers-data")
    suspend fun getAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String
    ): List<SupplierData>

    @POST("suppliers-data/bulkDelete")
    suspend fun deleteAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Body data: List<SupplierDataRequest>
    ): Response<Unit>

    @POST("suppliers-data")
    suspend fun upsertAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Body data: List<SupplierDataRequest>
    ): Response<Unit>
}