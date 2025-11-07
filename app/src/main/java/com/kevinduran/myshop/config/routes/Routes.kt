package com.kevinduran.myshop.config.routes

import android.net.Uri
import com.google.gson.Gson
import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.domain.models.Report
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.domain.models.SaleReturn
import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.domain.models.TransferPayment

sealed class Routes(val route: String) {

    data object Splash : Routes("splash")
    data object Welcome : Routes("welcome")
    data object Home : Routes("home")
    data object Suppliers : Routes("suppliers")
    data object CreateSupplier : Routes("createSupplier")
    data object CreateProduct : Routes("createProduct")
    data object CreateSale : Routes("createSale")
    data object SearchSales : Routes("searchSales")
    data object Employees : Routes("employees")
    data object CreateEmployee : Routes("createEmployee")
    data object Analytics : Routes("analytics")
    data object AnalyticsByDateRange : Routes("analyticsByDateRange")
    data object Reports : Routes("reports")
    data object CashClosure : Routes("cashClosure")
    data object TransferPayments: Routes("transferPayments")
    data object ReturnedSales: Routes("returnedSales")

    data object ReportsScreen : Routes("reportsScreen/{item}") {
        fun createRoute(supplier: Supplier): String {
            val json = Gson().toJson(supplier)
            val uri = Uri.encode(json)
            return "reportsScreen/$uri"
        }
    }

    data object SalePreview : Routes("salePreview/{item}") {
        fun createRoute(sale: Sale): String {
            val json = Gson().toJson(sale)
            val uri = Uri.encode(json)
            return "salePreview/$uri"
        }
    }

    data object UpdateEmployee : Routes("updateEmployee/{item}") {
        fun createRoute(employee: Employee): String {
            val json = Gson().toJson(employee)
            val uri = Uri.encode(json)
            return "updateEmployee/$uri"
        }
    }

    data object UpdateReport : Routes("updateReport/{item}") {
        fun createRoute(report: Report): String {
            val json = Gson().toJson(report)
            val uri = Uri.encode(json)
            return "createReport/$uri"
        }
    }

    data object UpdateProduct : Routes("updateProduct/{item}") {
        fun createRoute(product: Product): String {
            val json = Gson().toJson(product)
            val uri = Uri.encode(json)
            return "updateProduct/$uri"
        }
    }

    data object UpdateSale : Routes("updateSale/{item}") {
        fun createRoute(sale: Sale): String {
            val json = Gson().toJson(sale)
            val uri = Uri.encode(json)
            return "updateSale/$uri"
        }
    }

    data object UpdateSupplier : Routes("updateSupplier/{item}") {
        fun createRoute(supplier: Supplier): String {
            val json = Gson().toJson(supplier)
            val uri = Uri.encode(json)
            return "updateSupplier/$uri"
        }
    }

    data object CreateSupplierData : Routes("createSupplierData/{item}?type={type}") {
        fun createRoute(supplier: Supplier, type: String = "income"): String {
            val json = Gson().toJson(supplier)
            val supplierUri = Uri.encode(json)
            val typeUri = Uri.encode(type)
            return "createSupplierData/$supplierUri?type=$typeUri"
        }
    }

    data object SupplierData:Routes("supplierData/{item}") {
        fun createRoute(supplier: Supplier): String {
            val json = Gson().toJson(supplier)
            val uri = Uri.encode(json)
            return "supplierData/$uri"
        }
    }


    data object CreateSupplierDebtPayment : Routes("createSupplierDebtPayment/{item}") {
        fun createRoute(supplier: Supplier): String {
            val json = Gson().toJson(supplier)
            val uri = Uri.encode(json)
            return "createSupplierDebtPayment/$uri"
        }
    }

    data object SupplierDataPreview : Routes("supplierDataPreview/{item}") {
        fun createRoute(data: com.kevinduran.myshop.domain.models.SupplierData): String {
            val json = Gson().toJson(data)
            val uri = Uri.encode(json)
            return "supplierDataPreview/$uri"
        }
    }

    data object PaymentDetails : Routes("paymentDetails/{item}") {
        fun createRoute(payment: TransferPayment): String {
            val json = Gson().toJson(payment)
            val uri = Uri.encode(json)
            return "paymentDetails/$uri"
        }
    }

    data object ReturnDetails : Routes("returnDetails/{item}") {
        fun createRoute(returnEntity: SaleReturn): String {
            val json = Gson().toJson(returnEntity)
            val uri = Uri.encode(json)
            return "returnDetails/$uri"
        }
    }

    data object ProductPhoto : Routes("productPhoto/{item}") {
        fun createRoute(imagePath: String): String {
            val uri = Uri.encode(imagePath)
            return "productPhoto/$uri"
        }
    }

    data object ProductPreview : Routes("productPreview/{item}") {
        fun createRoute(product: Product): String {
            val json = Gson().toJson(product)
            val uri = Uri.encode(json)
            return "productPreview/$uri"
        }
    }

}
