package com.kevinduran.myshop.di

import com.kevinduran.myshop.domain.repositories.EmployeesRepository
import com.kevinduran.myshop.domain.repositories.ProductsRepository
import com.kevinduran.myshop.domain.repositories.ReportsRepository
import com.kevinduran.myshop.domain.repositories.SaleReturnsRepository
import com.kevinduran.myshop.domain.repositories.SalesRepository
import com.kevinduran.myshop.domain.repositories.SupplierDataRepository
import com.kevinduran.myshop.domain.repositories.SupplierRepository
import com.kevinduran.myshop.domain.repositories.TransferPaymentsRepository
import com.kevinduran.myshop.infrastructure.repositories.EmployeesRepositoryImpl
import com.kevinduran.myshop.infrastructure.repositories.ProductsRepositoryImpl
import com.kevinduran.myshop.infrastructure.repositories.ReportsRepositoryImpl
import com.kevinduran.myshop.infrastructure.repositories.SaleReturnsRepositoryImpl
import com.kevinduran.myshop.infrastructure.repositories.SalesRepositoryImpl
import com.kevinduran.myshop.infrastructure.repositories.SupplierDataRepositoryImpl
import com.kevinduran.myshop.infrastructure.repositories.SuppliersRepositoryImpl
import com.kevinduran.myshop.infrastructure.repositories.TransferPaymentsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun bindsEmployeesRepository(
        impl: EmployeesRepositoryImpl
    ): EmployeesRepository

    @Binds
    abstract fun bindsProductsRepository(
        impl: ProductsRepositoryImpl
    ): ProductsRepository

    @Binds
    abstract fun bindsSalesRepository(
        impl: SalesRepositoryImpl
    ): SalesRepository

    @Binds
    abstract fun bindsSuppliersRepository(
        impl: SuppliersRepositoryImpl
    ): SupplierRepository

    @Binds
    abstract fun bindsReportsRepository(
        impl: ReportsRepositoryImpl
    ): ReportsRepository

    @Binds
    abstract fun bindsSuppliersDataRepository(
        impl: SupplierDataRepositoryImpl
    ): SupplierDataRepository

    @Binds
    abstract fun bindsTransferPaymentsRepository(
        impl: TransferPaymentsRepositoryImpl
    ): TransferPaymentsRepository

    @Binds
    abstract fun bindsSaleReturnsRepository(
        impl: SaleReturnsRepositoryImpl
    ): SaleReturnsRepository

}
