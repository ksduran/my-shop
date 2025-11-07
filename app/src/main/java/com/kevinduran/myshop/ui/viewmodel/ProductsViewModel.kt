package com.kevinduran.myshop.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.kevinduran.myshop.config.extensions.generateProductImageFullPath
import com.kevinduran.myshop.config.extensions.sanitizeColor
import com.kevinduran.myshop.config.extensions.toInternalImageUri
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.domain.repositories.ProductsRepository
import com.kevinduran.myshop.domain.usecases.product.ProductUseCases
import com.kevinduran.myshop.infrastructure.model.ProductImage
import com.kevinduran.myshop.infrastructure.model.ProductVariant
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import com.kevinduran.myshop.infrastructure.services.PhotosServices
import com.kevinduran.myshop.infrastructure.services.retrofit.StorageService
import com.kevinduran.myshop.ui.states.ProductsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val productUseCases: ProductUseCases,
    private val preferences: UserPreferencesRepository,
    private val storageService: StorageService,
) : ViewModel() {

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    val allProducts: StateFlow<List<Product>> get() = _allProducts
    private val _selectedImages = mutableStateListOf<ProductImage>()
    val selectedImages: List<ProductImage> get() = _selectedImages
    private val _uiState = MutableStateFlow(ProductsState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val license = preferences.getLicenseId().first().toString()
            _uiState.update { it.copy(license = license) }
            getAll()
        }
    }

    fun selectedImagesClear() {
        _selectedImages.clear()
    }

    suspend fun addProduct(
        name: String,
        ref: String,
        salePrice: Int,
        purchasePrice: Int,
        supplierName: String,
        context: Context
    ) {
        _uiState.update { it.copy(loading = true) }
        val uuid = UUID.randomUUID().toString()
        val variants = generateVariants(context, uuid)
        if (variants.isFailure) {
            _uiState.update {
                it.copy(
                    loading = false,
                    error = "Error al subir las fotos al servidor, intente nuevamente"
                )
            }
            return
        }
        val result = productUseCases.addProduct(
            uuid,
            name,
            ref,
            salePrice,
            purchasePrice,
            supplierName,
            variants.getOrElse { "" }
        )
        result.onSuccess {
            selectedImagesClear()
            _uiState.update { it.copy(selectedItems = emptyList(), loading = false) }
        }
        result.handleResult()
    }

    fun addImage(uri: Uri, context: Context? = null) {
        val finalUri = context?.let {
            try {
                val tempFile = File(it.cacheDir, "temp_${UUID.randomUUID()}.jpg")
                it.contentResolver.openInputStream(uri)?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                tempFile.toUri()
            } catch (_: Exception) {
                uri
            }
        } ?: uri

        _selectedImages.add(ProductImage(finalUri, ""))
    }


    suspend fun deleteProduct(entity: Product) {
        withContext(Dispatchers.IO) {
            _uiState.update { it.copy(loading = true) }
            val result = productUseCases.deleteProduct(entity)
            result.onSuccess {
                updateItems()
                _uiState.update { it.copy(loading = false, selectedItems = emptyList()) }
            }
            result.handleResult()
        }
    }

    suspend fun getAll() {
        _uiState.update { it.copy(loading = true) }
        updateItems()
        _uiState.update { it.copy(loading = false) }
    }

    private suspend fun updateItems() {
        repository.get()
            .onSuccess {
                _allProducts.value = it
            }.onFailure { error ->
                _uiState.update { it.copy(error = "${error.message}", loading = false) }
            }

    }

    fun getFirstImage(
        entity: Product
    ): String {
        if (entity.variants.isEmpty()) return ""
        val variants = Gson().fromJson(entity.variants, Array<ProductVariant>::class.java)
        if (variants.isEmpty()) return ""
        return variants[0].image.generateProductImageFullPath(uiState.value.license)
    }

    fun getFirstImageFileName(entity: Product): String {
        if (entity.variants.isEmpty()) return ""
        val variants = Gson().fromJson(entity.variants, Array<ProductVariant>::class.java)
        if (variants.isEmpty()) return ""
        return variants[0].image
    }

    fun getImageByColor(license: String, entity: Product, color: String): String? {
        val variants = Gson().fromJson(entity.variants, Array<ProductVariant>::class.java)
        val variant = variants.firstOrNull { it.color == color }
        return variant?.image?.ifBlank { "" }?.generateProductImageFullPath(license)
    }

    fun getImageFileNameByColor(entity: Product, color: String): String? {
        val variants = Gson().fromJson(entity.variants, Array<ProductVariant>::class.java)
        return variants.firstOrNull { it.color == color }?.image
    }

    fun getVariants(context: Context, entity: Product): List<ProductVariant> {
        if (entity.variants.isEmpty()) return emptyList()
        val variants = Gson().fromJson(entity.variants, Array<ProductVariant>::class.java).toList()
        variants.forEach { variant ->
            val uri = variant.image.toInternalImageUri(context)
            addImage(uri)
            updateColor(uri, variant.color)
        }
        return variants
    }

    fun getColors(entity: Product): List<String> {
        val variants = Gson().fromJson(entity.variants, Array<ProductVariant>::class.java)
        val colors = mutableStateListOf<String>()
        for (v in variants) {
            colors.add(v.color)
        }
        return colors
    }


    private suspend fun generateVariants(context: Context, uuid: String): Result<String> {
        val variants = mutableStateListOf<ProductVariant>()
        val license = preferences.getLicenseId().first().toString()
        for (image in selectedImages) {
            val fileName = "${uuid}_${image.color.sanitizeColor()}.jpg"
            val destFile = File(context.filesDir, fileName)
            if (image.image.scheme == "file") {
                val from = File(image.image.path!!)
                if (from.absolutePath != destFile.absolutePath) {
                    val result = PhotosServices.put(
                        context = context,
                        uri = image.image,
                        license = license,
                        service = storageService,
                        type = "product",
                        fileName = fileName,
                    )
                    if (result.isFailure) return Result.failure(Exception("Failed uploading image"))
                    if (from.name.startsWith("temp_")) from.delete()
                }
            } else {
                val result = PhotosServices.put(
                    context = context,
                    uri = image.image,
                    license = license,
                    service = storageService,
                    type = "product",
                    fileName = fileName,
                )
                if (result.isFailure) return Result.failure(Exception("Failed uploading image"))
            }

            val variant = ProductVariant(
                color = image.color,
                image = fileName,
                stock = 0
            )
            variants.add(variant)
        }
        return Result.success(Gson().toJson(variants))
    }

    private suspend fun generateVariants(context: Context, entity: Product): Result<String> {
        val variants = mutableListOf<ProductVariant>()
        val license = preferences.getLicenseId().first().toString()
        for (image in selectedImages) {
            val color = image.color
            val fileName = "${entity.uuid}_${color.sanitizeColor()}.jpg"
            val destFile = File(context.filesDir, fileName)
            if (image.image.scheme == "file") {
                val from = File(image.image.path!!)
                if (from.absolutePath != destFile.absolutePath) {
                    val result = PhotosServices.put(
                        context = context,
                        uri = image.image,
                        license = license,
                        service = storageService,
                        type = "product",
                        fileName = fileName,
                    )
                    if (result.isFailure) return Result.failure(Exception("Failed uploading temp image"))
                    if (from.name.startsWith("temp_")) from.delete()
                }
            } else {
                val result = PhotosServices.put(
                    context = context,
                    uri = image.image,
                    license = license,
                    service = storageService,
                    type = "product",
                    fileName = fileName,
                )
                if (result.isFailure) return Result.failure(Exception("Failed uploading old image"))
            }

            val variant = ProductVariant(
                color = color,
                image = fileName,
                stock = 0
            )
            variants.add(variant)
        }

        return Result.success(Gson().toJson(variants))
    }

    suspend fun getProductByUuid(productId: String): Product? {
        _uiState.update { it.copy(loading = true) }
        return withContext(Dispatchers.IO) {
            val result = repository.getByUuid(productId)
            _uiState.update { it.copy(loading = false) }
            result.getOrNull()
        }
    }

    suspend fun updateProduct(
        name: String,
        ref: String,
        salePrice: Int,
        purchasePrice: Int,
        supplierName: String,
        entity: Product,
        context: Context
    ) {
        _uiState.update { it.copy(loading = true, error = "") }
        val variants = generateVariants(context, entity)
        variants.onFailure { error ->
            _uiState.update {
                it.copy(
                    loading = false,
                    error = "Error al subir las fotos al servidor, intente nuevamente: ${error.message}"
                )
            }
            return
        }
        variants.onSuccess { value ->
            val result = productUseCases.updateProduct(
                entity,
                name,
                ref,
                salePrice,
                purchasePrice,
                supplierName,
                value
            )
            result.onSuccess {
                selectedImagesClear()
                _uiState.update {
                    it.copy(
                        loading = false,
                        selectedItems = emptyList()
                    )
                }
            }
            result.handleResult()
        }

    }

    fun updateColor(uri: Uri, color: String) {
        val index = _selectedImages.indexOfFirst { it.image == uri }
        if (index != -1) {
            _selectedImages[index] = ProductImage(uri, color)
        }
    }

    fun removeImage(uri: Uri) {
        _selectedImages.removeIf { it.image == uri }
    }

    fun toggleSelectedItem(entity: Product) {
        val list = uiState.value.selectedItems.toMutableList()
        if (list.contains(entity)) {
            list.remove(entity)
        } else {
            list.add(entity)
        }
        _uiState.update {
            it.copy(selectedItems = list)
        }
    }

    fun selectAll() {
        var list = _uiState.value.selectedItems
        list = if (list.size == _allProducts.value.size) {
            emptyList()
        } else {
            allProducts.value
        }
        _uiState.update { it.copy(selectedItems = list) }
    }

    suspend fun deleteAll(entities: List<Product>) {
        withContext(Dispatchers.IO) {
            productUseCases.deleteProduct(entities)
                .onSuccess {
                    updateItems()
                    _uiState.update {
                        it.copy(
                            loading = false,
                            selectedItems = emptyList()
                        )
                    }
                }
                .handleResult()
        }
    }

    fun setError(error: String) {
        _uiState.update { it.copy(error = error, loading = false) }
    }

    private fun Result<Unit>.handleResult() {
        onFailure { error ->
            _uiState.update {
                it.copy(
                    loading = false,
                    error = "${error.message}"
                )
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(error = "") }
    }

}