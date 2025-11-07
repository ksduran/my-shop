package com.kevinduran.myshop.di

import com.kevinduran.myshop.domain.datasources.EmployeesDatasource
import com.kevinduran.myshop.domain.datasources.ProductsDatasource
import com.kevinduran.myshop.domain.datasources.ReportsDatasource
import com.kevinduran.myshop.domain.datasources.SaleReturnsDatasource
import com.kevinduran.myshop.domain.datasources.SalesDatasource
import com.kevinduran.myshop.domain.datasources.SupplierDataDatasource
import com.kevinduran.myshop.domain.datasources.SuppliersDatasource
import com.kevinduran.myshop.domain.datasources.TransferPaymentsDatasource
import com.kevinduran.myshop.infrastructure.datasources.http.HttpEmployeeDatasource
import com.kevinduran.myshop.infrastructure.datasources.http.HttpProductsDatasource
import com.kevinduran.myshop.infrastructure.datasources.http.HttpReportsDatasource
import com.kevinduran.myshop.infrastructure.datasources.http.HttpSaleReturnsDatasource
import com.kevinduran.myshop.infrastructure.datasources.http.HttpSalesDatasource
import com.kevinduran.myshop.infrastructure.datasources.http.HttpSupplierDataDatasource
import com.kevinduran.myshop.infrastructure.datasources.http.HttpSupplierDatasource
import com.kevinduran.myshop.infrastructure.datasources.http.HttpTransferPaymentsDatasource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DatasourceModule {

    @Binds
    abstract fun bindsEmployeesDatasource(
        datasource: HttpEmployeeDatasource
    ): EmployeesDatasource

    @Binds
    abstract fun bindsProductsDatasource(
        datasource: HttpProductsDatasource
    ): ProductsDatasource

    @Binds
    abstract fun bindsSalesDatasource(
        datasource: HttpSalesDatasource
    ): SalesDatasource

    @Binds
    abstract fun bindsSuppliersDatasource(
        datasource: HttpSupplierDatasource
    ): SuppliersDatasource

    @Binds
    abstract fun bindsReportsDatasource(
        datasource: HttpReportsDatasource
    ): ReportsDatasource

    @Binds
    abstract fun bindsSupplierDataDatasource(
        datasource: HttpSupplierDataDatasource
    ): SupplierDataDatasource

    @Binds
    abstract fun bindsTransferPaymentsDatasource(
        datasource: HttpTransferPaymentsDatasource
    ): TransferPaymentsDatasource

    @Binds
    abstract fun bindsSaleReturnsDatasource(
        datasource: HttpSaleReturnsDatasource
    ): SaleReturnsDatasource
}