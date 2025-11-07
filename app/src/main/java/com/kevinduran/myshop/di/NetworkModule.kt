package com.kevinduran.myshop.di

import com.kevinduran.myshop.infrastructure.services.retrofit.EmployeesService
import com.kevinduran.myshop.infrastructure.services.retrofit.ProductsService
import com.kevinduran.myshop.infrastructure.services.retrofit.SaleReturnsService
import com.kevinduran.myshop.infrastructure.services.retrofit.SalesService
import com.kevinduran.myshop.infrastructure.services.retrofit.StorageService
import com.kevinduran.myshop.infrastructure.services.retrofit.SupplierDataService
import com.kevinduran.myshop.infrastructure.services.retrofit.SuppliersService
import com.kevinduran.myshop.infrastructure.services.retrofit.TransferPaymentsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://keranstore.com/api/borrowed/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesSalesService(retrofit: Retrofit): SalesService {
        return retrofit.create(SalesService::class.java)
    }

    @Provides
    @Singleton
    fun providesEmployeesService(retrofit: Retrofit): EmployeesService {
        return retrofit.create(EmployeesService::class.java)
    }

    @Provides
    @Singleton
    fun providesProductsService(retrofit: Retrofit): ProductsService {
        return retrofit.create(ProductsService::class.java)
    }

    @Provides
    @Singleton
    fun providesSaleReturnsService(retrofit: Retrofit): SaleReturnsService {
        return retrofit.create(SaleReturnsService::class.java)
    }

    @Provides
    @Singleton
    fun providesSupplierDataService(retrofit: Retrofit): SupplierDataService {
        return retrofit.create(SupplierDataService::class.java)
    }

    @Provides
    @Singleton
    fun providesSuppliersService(retrofit: Retrofit): SuppliersService {
        return retrofit.create(SuppliersService::class.java)
    }

    @Provides
    @Singleton
    fun providesTransferPaymentsService(retrofit: Retrofit): TransferPaymentsService {
        return retrofit.create(TransferPaymentsService::class.java)
    }

    @Provides
    @Singleton
    fun providesStorageService(retrofit: Retrofit): StorageService {
        return retrofit.create(StorageService::class.java)
    }

}