package com.kevinduran.myshop.config.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.kevinduran.myshop.config.routes.Routes
import com.kevinduran.myshop.domain.models.Employee
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.domain.models.SupplierData
import com.kevinduran.myshop.ui.views.analytics.AnalyticsByDateRangeScreen
import com.kevinduran.myshop.ui.views.analytics.AnalyticsScreen
import com.kevinduran.myshop.ui.views.cashclosure.CashClosureScreen
import com.kevinduran.myshop.ui.views.employees.CreateEmployeeScreen
import com.kevinduran.myshop.ui.views.employees.EmployeesScreen
import com.kevinduran.myshop.ui.views.home.HomeScreen
import com.kevinduran.myshop.ui.views.products.CreateProductScreen
import com.kevinduran.myshop.ui.views.products.ProductDetailsScreen
import com.kevinduran.myshop.ui.views.products.ProductPhotoScreen
import com.kevinduran.myshop.ui.views.reportsales.ReportsScreen
import com.kevinduran.myshop.ui.views.reportsales.ReportsSuppliersScreen
import com.kevinduran.myshop.ui.views.sales.CreateSaleScreen
import com.kevinduran.myshop.ui.views.sales.SaleDetailsScreen
import com.kevinduran.myshop.ui.views.splash.SplashScreen
import com.kevinduran.myshop.ui.views.suppliers.CreateSupplierDebtPaymentScreen
import com.kevinduran.myshop.ui.views.suppliers.CreateSupplierScreen
import com.kevinduran.myshop.ui.views.suppliers.CreateSuppliersDataIncomeScreen
import com.kevinduran.myshop.ui.views.suppliers.SupplierDataPreviewScreen
import com.kevinduran.myshop.ui.views.suppliers.SupplierDataScreen
import com.kevinduran.myshop.ui.views.suppliers.SuppliersScreen
import com.kevinduran.myshop.ui.views.welcome.WelcomeScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) { SplashScreen(navController) }
        composable(Routes.Welcome.route) { WelcomeScreen(navController) }
        composable(Routes.Home.route) { HomeScreen(navController) }
        composable(Routes.Suppliers.route) { SuppliersScreen(navController) }
        composable(Routes.CreateProduct.route) { CreateProductScreen(navController) }
        composable(Routes.CreateSale.route) { CreateSaleScreen(navController) }
        composable(Routes.CreateSupplier.route) { CreateSupplierScreen(navController) }
        composable(Routes.CreateEmployee.route) { CreateEmployeeScreen(navController) }
        composable(Routes.Employees.route) { EmployeesScreen(navController) }
        composable(Routes.CashClosure.route) { CashClosureScreen(navController) }
        composable(Routes.Analytics.route) { AnalyticsScreen(navController) }
        composable(Routes.AnalyticsByDateRange.route) { AnalyticsByDateRangeScreen(navController) }
        composable(Routes.Reports.route) { ReportsSuppliersScreen(navController) }

        composable(Routes.CreateSupplierData.route) { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item")
            val type = backStackEntry.arguments?.getString("type") ?: "income"
            val supplier = Gson().fromJson(Uri.decode(item), Supplier::class.java)
            CreateSuppliersDataIncomeScreen(supplier, navController, type)
        }

        composable(Routes.CreateSupplierDebtPayment.route) { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item")
            val supplier = Gson().fromJson(Uri.decode(item), Supplier::class.java)
            CreateSupplierDebtPaymentScreen(supplier, navController)
        }

        composable(Routes.SupplierData.route) { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item")
            val supplier = Gson().fromJson(Uri.decode(item), Supplier::class.java)
            SupplierDataScreen(supplier, navController)
        }

        composable(Routes.SupplierDataPreview.route) { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item")
            val data = Gson().fromJson(Uri.decode(item), SupplierData::class.java)
            SupplierDataPreviewScreen(data, navController)
        }

        composable(Routes.SalePreview.route) { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item")
            val uri = Uri.decode(item)
            val sale: Sale = Gson().fromJson(uri, Sale::class.java)
            SaleDetailsScreen(sale, navController)
        }

        composable(Routes.UpdateProduct.route) { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item")
            var product: Product? = null
            item?.let {
                val uri = Uri.decode(it)
                product = Gson().fromJson(uri, Product::class.java)
            }
            CreateProductScreen(navController, item = product)
        }

        composable(Routes.UpdateSale.route) { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item")
            var sale: Sale? = null
            item?.let {
                val uri = Uri.decode(item)
                sale = Gson().fromJson(uri, Sale::class.java)
            }
            CreateSaleScreen(navController, sale)
        }

        composable(Routes.UpdateSupplier.route) { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item")
            var supplier: Supplier? = null
            item?.let {
                supplier = Gson().fromJson(Uri.decode(it), Supplier::class.java)
            }
            CreateSupplierScreen(supplier = supplier, navController = navController)
        }

        composable(Routes.ProductPhoto.route) { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item")
            val imagePath = Uri.decode(item)
            ProductPhotoScreen(imagePath, navController)
        }

        composable(Routes.ProductPreview.route) { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item")
            val product = Gson().fromJson(Uri.decode(item), Product::class.java)
            ProductDetailsScreen(product = product, navController = navController)
        }
        composable(Routes.UpdateEmployee.route) { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item")
            val uri = Uri.decode(item)
            val employee = Gson().fromJson(uri, Employee::class.java)
            CreateEmployeeScreen(navController, employee = employee)
        }

        composable(Routes.ReportsScreen.route) { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item")
            val supplier = Gson().fromJson(Uri.decode(item), Supplier::class.java)
            ReportsScreen(supplier, navController)
        }

    }
}