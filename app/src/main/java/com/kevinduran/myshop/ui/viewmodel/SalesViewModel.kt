package com.kevinduran.myshop.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.config.constants.Payment
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.domain.models.SaleReturnProduct
import com.kevinduran.myshop.domain.models.TransferPaymentProduct
import com.kevinduran.myshop.domain.repositories.SalesRepository
import com.kevinduran.myshop.domain.usecases.sale.SaleUseCases
import com.kevinduran.myshop.infrastructure.model.EarningsSummary
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import com.kevinduran.myshop.infrastructure.services.PhotosServices
import com.kevinduran.myshop.infrastructure.services.retrofit.StorageService
import com.kevinduran.myshop.ui.states.SalesOrder
import com.kevinduran.myshop.ui.states.SalesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.WeekFields
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SalesViewModel @Inject constructor(
    private val repository: SalesRepository,
    private val saleUseCases: SaleUseCases,
    private val preferences: UserPreferencesRepository,
    private val storageService: StorageService
) : ViewModel() {

    private val _allItems = MutableStateFlow<List<Sale>>(emptyList())
    val allItems = _allItems.asStateFlow()

    private val _uiState = MutableStateFlow(SalesState())
    val uiState = _uiState.asStateFlow()

    private val isAdmin = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            val license = preferences.getLicenseId().first().toString()
            isAdmin.value = preferences.getIsAdmin().first()
            _uiState.update { it.copy(license = license) }
            getAll()
        }
    }

    fun getAll() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(loading = true) }
            val currentState = uiState.value
            val dateRange = currentState.dateRange
            val isToday = isSameDay(dateRange.first, Instant.now().toEpochMilli())
            val searchSales = currentState.searchSales
            val status = currentState.paymentStatus
            val raisedBy = if (isAdmin.value) {
                currentState.raisedBy
            } else {
                preferences.getUserName().first()
            }

            var list = runCatching {
                if (isToday) {
                    repository.getToday(
                        companyName = searchSales,
                        paymentStatus = status,
                        raisedBy = raisedBy,
                        from = dateRange.first,
                        to = dateRange.second
                    ).getOrThrow()
                } else {
                    repository.getByDateRange(
                        companyName = searchSales,
                        paymentStatus = status,
                        raisedBy = raisedBy,
                        from = dateRange.first,
                        to = dateRange.second
                    ).getOrThrow()
                }
            }.getOrElse {
                emptyList()
            }

            list = when (currentState.order) {
                SalesOrder.ALPHABETICAL -> list.sortedBy { it.companyName.lowercase() }
                SalesOrder.CREATED_AT -> list.sortedByDescending { it.createdAt }
            }
            _allItems.value = list
            _uiState.update { it.copy(loading = false) }
        }
    }

    suspend fun addAllSales(
        companyName: String,
        color: String,
        size: String,
        sizeR: String,
        salePrice: Int,
        image: String,
        entities: List<Product>
    ) {
        withContext(Dispatchers.IO) {
            _uiState.update { it.copy(loading = true) }
            saleUseCases.add(
                companyName = companyName,
                color = color,
                size = size,
                sizeR = sizeR,
                salePrice = salePrice,
                image = image,
                products = entities
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        loading = false,
                        selectedItems = emptyList(),
                        error = ""
                    )
                }
                getAll()
            }.handleResult()
        }
    }

    suspend fun assignUserToSales(user: String, entities: List<Sale>) {
        _uiState.update { it.copy(loading = true) }
        saleUseCases.assignUserToSales(entities, user).handleResult()
        getAll()
    }

    suspend fun deleteAll(entities: List<Sale>) {
        _uiState.update { it.copy(loading = true) }
        saleUseCases.delete(entities).handleResult()
        getAll()
    }

    suspend fun getEarningByDateRange(from: Long, to: Long): EarningsSummary {
        _uiState.update { it.copy(loading = true) }
        val value = repository.getEarningsSummary(from, to)
            .getOrElse { error ->
                _uiState.update {
                    it.copy(
                        error = "${error.message}",
                        loading = false
                    )
                }
                EarningsSummary(0, 0)
            }
        _uiState.update { it.copy(loading = false) }
        return value
    }

    suspend fun getTotalCashByUser(user: String): Int {
        _uiState.update { it.copy(loading = true) }
        val value = repository.getTotalCashByUser(user)
            .getOrElse { error ->
                _uiState.update {
                    it.copy(
                        loading = false,
                        error = "${error.message}"
                    )
                }
                0
            }
        _uiState.update { it.copy(loading = false) }
        return value
    }

    fun getCurrentDayRange(): Pair<Long, Long> {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)
        val start = today.atStartOfDay(zone).toInstant().toEpochMilli()
        val end = today.plusDays(1).atStartOfDay(zone).minusNanos(1).toInstant().toEpochMilli()
        return Pair(start, end)
    }

    fun getCurrentWeekRange(): Pair<Long, Long> {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)
        val firstDayOfWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
        val lastDayOfWeek = firstDayOfWeek.plusDays(6)

        val start = firstDayOfWeek.atStartOfDay(zone).toInstant().toEpochMilli()
        val end =
            lastDayOfWeek.plusDays(1).atStartOfDay(zone).minusNanos(1).toInstant().toEpochMilli()
        return Pair(start, end)
    }

    fun getCurrentMonthRange(): Pair<Long, Long> {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)
        val startOfMonth = today.withDayOfMonth(1)
        val endOfMonth = today.withDayOfMonth(today.lengthOfMonth())

        val start = startOfMonth.atStartOfDay(zone).toInstant().toEpochMilli()
        val end = endOfMonth.plusDays(1).atStartOfDay(zone).minusNanos(1).toInstant().toEpochMilli()
        return Pair(start, end)
    }

    fun getCurrentYearRange(): Pair<Long, Long> {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)
        val startOfYear = today.withDayOfYear(1)
        val endOfYear = today.withDayOfYear(today.lengthOfYear())

        val start = startOfYear.atStartOfDay(zone).toInstant().toEpochMilli()
        val end = endOfYear.plusDays(1).atStartOfDay(zone).minusNanos(1).toInstant().toEpochMilli()
        return Pair(start, end)
    }

    private fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = timestamp1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = timestamp2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    suspend fun updateSale(
        companyName: String,
        color: String,
        size: String,
        sizeR: String,
        salePrice: Int,
        purchasePrice: Int,
        entity: Sale
    ) {
        withContext(Dispatchers.IO) {
            _uiState.update { it.copy(loading = true) }
            val copy = entity.copy(
                companyName = companyName,
                color = color,
                size = size,
                sizeR = sizeR,
                salePrice = salePrice,
                purchasePrice = purchasePrice,
                syncStatus = 0,
                updatedBy = Firebase.auth.currentUser?.displayName
                    ?: Firebase.auth.currentUser?.email
                    ?: "",
                updatedAt = Instant.now().toEpochMilli()
            )
            repository.update(copy).handleResult()
            getAll()
        }
    }

    suspend fun setSaleChange(
        entity: Sale,
        productId: String,
        size: String,
        color: String
    ) {
        _uiState.update { it.copy(loading = true) }
        withContext(Dispatchers.IO) {
            val updatedEntity = entity.copy(
                syncStatus = 0,
                updatedBy = Firebase.auth.currentUser?.displayName
                    ?: Firebase.auth.currentUser?.email
                    ?: "",
                updatedAt = Instant.now().toEpochMilli(),
                paymentStatus = Payment.Change.status,
                changedProductId = productId,
                changedSize = size,
                changedProductColor = color
            )
            repository.update(updatedEntity).handleResult()
            getAll()
        }
    }

    fun setFilterByUser(user: String) {
        _uiState.update { it.copy(raisedBy = user) }
        getAll()
    }

    suspend fun registerTransferPayment(
        imageFileName: String,
        products: List<TransferPaymentProduct>,
        storeName: String,
        updatedSales: List<Sale>
    ): Boolean {
        _uiState.update { it.copy(loading = true) }
        return saleUseCases.registerTransferPayment(
            storeName = storeName,
            imageFileName = imageFileName,
            products = products,
            updatedSales = updatedSales
        ).onSuccess {
            _uiState.update {
                it.copy(
                    loading = false,
                    error = "",
                    selectedItems = emptyList()
                )
            }
        }.isSuccess
    }

    fun setSalePaymentStatus(
        paymentStatus: Payment,
        entities: List<Sale>
    ) = viewModelScope.launch {
        _uiState.update { it.copy(loading = true) }
        saleUseCases.updateSalePaymentStatus(entities, paymentStatus)
            .handleResult()
        getAll()
    }

    suspend fun registerSaleReturn(
        imageFileName: String,
        products: List<SaleReturnProduct>,
        storeName: String,
        entities: List<Sale>
    ): Boolean {
        _uiState.update { it.copy(loading = true) }
        return saleUseCases.registerSaleReturn(
            storeName = storeName,
            imageFileName = imageFileName,
            products = products,
            updatedSales = entities
        ).onSuccess {
            _uiState.update {
                it.copy(
                    loading = false,
                    error = "",
                    selectedItems = emptyList()
                )
            }
            getAll()
        }.isSuccess
    }

    fun setPaymentStatus(paymentStatus: String) {
        _uiState.update { it.copy(paymentStatus = paymentStatus) }
        getAll()
    }

    fun setDateRange(start: Long, end: Long) {
        _uiState.update { currentState ->
            currentState.copy(
                dateRange = Pair(start, end),
                selectedItems = emptyList()
            )
        }
        getAll()
    }

    fun searchSales(value: String) {
        _uiState.update { currentState ->
            currentState.copy(searchSales = "${value}%")
        }
        getAll()
    }

    fun toggleSelectedItems(entity: Sale) {
        _uiState.update { currentState ->
            if (currentState.selectedItems.contains(entity)) {
                currentState.copy(selectedItems = currentState.selectedItems - entity)
            } else {
                currentState.copy(selectedItems = currentState.selectedItems + entity)
            }
        }
    }

    fun selectAllSales() {
        _uiState.update {
            if (it.selectedItems.size == allItems.value.size) {
                it.copy(selectedItems = emptyList())
            } else {
                it.copy(selectedItems = allItems.value)
            }
        }
    }

    fun toggleOrder() {
        _uiState.update { currentState ->
            val newOrder = when (currentState.order) {
                SalesOrder.ALPHABETICAL -> SalesOrder.CREATED_AT
                SalesOrder.CREATED_AT -> SalesOrder.ALPHABETICAL
            }
            currentState.copy(order = newOrder)
        }
        getAll()
    }

    fun <T> Result<T>.handleResult() {

        onSuccess {
            _uiState.update {
                it.copy(
                    loading = false,
                    selectedItems = emptyList(),
                    error = ""
                )
            }
        }

        onFailure { error ->
            _uiState.update {
                it.copy(
                    error = "${error.message}",
                    loading = false
                )
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(error = "") }
    }

    suspend fun uploadPaymentOrReturnImage(
        context: Context,
        uri: Uri,
        type: String,
        fileName: String,
        sales: List<Sale>
    ): Boolean {
        _uiState.update { it.copy(loading = true) }
        val license = preferences.getLicenseId().first().toString()
        val result = PhotosServices.put(context, uri, license, storageService, type, fileName)

        return result.fold(
            onSuccess = {
                if (type == "payment") {
                    val products = sales.map { sale ->
                        TransferPaymentProduct(sale.productId, sale.size, sale.image)
                    }
                    val storeName = sales.firstOrNull()?.companyName ?: "Unknown"
                    registerTransferPayment(fileName, products, storeName, sales)
                } else {
                    val products = sales.map { sale ->
                        SaleReturnProduct(sale.productId, sale.size, sale.image)
                    }
                    val storeName = sales.firstOrNull()?.companyName ?: ""
                    registerSaleReturn(fileName, products, storeName, sales)
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        loading = false,
                        error = "Error al subir la imagen: ${error.message}"
                    )
                }
                false
            }
        )
    }
}