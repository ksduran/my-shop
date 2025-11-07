package com.kevinduran.myshop.domain.models

/**
 * Permissions assigned to an employee. All values are represented as Int where
 * 0 is false and 1 is true.
 */
data class EmployeePermissions(
    val chargeControl: Int = 1,
    val createSales: Int = 0,
    val createProducts: Int = 0,
    val createEmployees: Int = 0,
    val createSuppliers: Int = 0,
    val updateSales: Int = 0,
    val updateProducts: Int = 0,
    val updateEmployees: Int = 0,
    val updateSuppliers: Int = 0,
    val deleteSales: Int = 0,
    val deleteProducts: Int = 0,
    val deleteEmployees: Int = 0,
    val deleteSuppliers: Int = 0,
    val fullSalesControl: Int = 0,
    val fullProductsControl: Int = 0,
    val fullEmployeesControl: Int = 0,
    val fullSuppliersControl: Int = 0,
    val seeSalesDetails: Int = 0,
    val seeProductsDetails: Int = 0,
    val seeSalesReports: Int = 0,
    val seeFinanceReport: Int = 0
)