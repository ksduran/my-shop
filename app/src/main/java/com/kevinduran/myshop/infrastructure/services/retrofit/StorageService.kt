package com.kevinduran.myshop.infrastructure.services.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface StorageService {
    @Multipart
    @POST("storage/upload-product")
    suspend fun uploadProduct(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Part("fileName") fileName: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<Unit>

    @Multipart
    @POST("storage/upload-return")
    suspend fun uploadReturn(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Part("fileName") fileName: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<Unit>

    @Multipart
    @POST("storage/upload-payment")
    suspend fun uploadPayment(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Part("fileName") fileName: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<Unit>
}