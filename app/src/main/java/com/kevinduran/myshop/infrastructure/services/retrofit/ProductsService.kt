package com.kevinduran.myshop.infrastructure.services.retrofit

import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.infrastructure.request.ProductRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductsService {
    @GET("products")
    suspend fun getAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
    ): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getById(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Path("id") id: String
    ): Response<Product?>

    @POST("products/bulkDelete")
    suspend fun deleteAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Body products: List<ProductRequest>
    ): Response<Unit>

    @POST("products")
    suspend fun upsertAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Body products: List<ProductRequest>
    ): Response<Unit>
}