package com.kevinduran.myshop.infrastructure.mappers

import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.infrastructure.request.EmployeeRequest

fun Employee.toRequest(license: String): EmployeeRequest {
    return EmployeeRequest(
        uuid = uuid,
        name = name,
        user = user,
        chargeControl = chargeControl,
        createSales = createSales,
        createProducts = createProducts,
        createEmployees = createEmployees,
        createSuppliers = createSuppliers,
        updateSales = updateSales,
        updateProducts = updateProducts,
        updateEmployees = updateEmployees,
        updateSuppliers = updateSuppliers,
        deleteSales = deleteSales,
        deleteProducts = deleteProducts,
        deleteEmployees = deleteEmployees,
        deleteSuppliers = deleteSuppliers,
        fullSalesControl = fullSalesControl,
        fullProductsControl = fullProductsControl,
        fullEmployeesControl = fullEmployeesControl,
        fullSuppliersControl = fullSuppliersControl,
        seeSalesDetails = seeSalesDetails,
        seeProductsDetails = seeProductsDetails,
        seeSalesReports = seeSalesReports,
        seeFinanceReport = seeFinanceReport,
        status = status,
        syncStatus = syncStatus,
        deleted = deleted,
        raisedBy = raisedBy,
        updatedBy = updatedBy,
        updatedAt = updatedAt,
        createdAt = createdAt,
        license = license
    )
}
