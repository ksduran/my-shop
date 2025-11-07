package com.kevinduran.myshop.domain.usecases.employee

import com.kevinduran.myshop.di.IoDispatcher
import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.domain.models.EmployeePermissions
import com.kevinduran.myshop.domain.providers.CurrentUserProvider
import com.kevinduran.myshop.domain.providers.TimerProvider
import com.kevinduran.myshop.domain.repositories.EmployeesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateEmployeeUseCase @Inject constructor(
    private val repository: EmployeesRepository,
    private val currentUserProvider: CurrentUserProvider,
    private val timerProvider: TimerProvider,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        entity: Employee,
        name: String,
        user: String,
        permissions: EmployeePermissions
    ): Result<Unit> {
        return withContext(ioDispatcher) {
            try {
                val userName = currentUserProvider.getNameOrEmail()
                val now = timerProvider.getNowMillis()
                val updatedEntity = entity.copy(
                    name = name,
                    user = user,
                    chargeControl = permissions.chargeControl,
                    createSales = permissions.createSales,
                    createProducts = permissions.createProducts,
                    createEmployees = permissions.createEmployees,
                    createSuppliers = permissions.createSuppliers,
                    updateSales = permissions.updateSales,
                    updateProducts = permissions.updateProducts,
                    updateEmployees = permissions.updateEmployees,
                    updateSuppliers = permissions.updateSuppliers,
                    deleteSales = permissions.deleteSales,
                    deleteProducts = permissions.deleteProducts,
                    deleteEmployees = permissions.deleteEmployees,
                    deleteSuppliers = permissions.deleteSuppliers,
                    fullSalesControl = permissions.fullSalesControl,
                    fullProductsControl = permissions.fullProductsControl,
                    fullEmployeesControl = permissions.fullEmployeesControl,
                    fullSuppliersControl = permissions.fullSuppliersControl,
                    seeSalesDetails = permissions.seeSalesDetails,
                    seeProductsDetails = permissions.seeProductsDetails,
                    seeSalesReports = permissions.seeSalesReports,
                    seeFinanceReport = permissions.seeFinanceReport,
                    syncStatus = 0,
                    updatedBy = userName,
                    updatedAt = now
                )
                repository.update(updatedEntity)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
