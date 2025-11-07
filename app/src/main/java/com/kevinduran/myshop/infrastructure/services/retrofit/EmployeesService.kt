package com.kevinduran.myshop.infrastructure.services.retrofit

import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.infrastructure.request.EmployeeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface EmployeesService {

    @GET("employees")
    suspend fun getAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String
    ): Response<List<Employee>>

    @POST("employees/bulkDelete")
    suspend fun deleteAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Body employees: List<EmployeeRequest>
    ): Response<Any>

    @POST("employees")
    suspend fun upsertAll(
        @Header("X-License-Code") license: String,
        @Header("X-Package-Name") packageName: String,
        @Body employees: List<EmployeeRequest>
    ): Response<Any>

}