package com.kevinduran.myshop.infrastructure.services.retrofit

import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.infrastructure.request.SupplierRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SuppliersService {
    @GET("suppliers")
    suspend fun getAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String
    ): List<Supplier>

    @POST("suppliers/bulkDelete")
    suspend fun deleteAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Body suppliers: List<SupplierRequest>
    ): Response<Unit>

    @POST("suppliers")
    suspend fun upsertAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Body suppliers: List<SupplierRequest>
    ): Response<Unit>
}