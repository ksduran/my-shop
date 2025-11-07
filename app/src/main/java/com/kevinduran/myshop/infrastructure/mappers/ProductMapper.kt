package com.kevinduran.myshop.infrastructure.mappers

import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.infrastructure.request.ProductRequest

fun Product.toRequest(license: String): ProductRequest {
    return ProductRequest(
        uuid = uuid,
        name = name,
        ref = ref,
        salePrice = salePrice,
        purchasePrice = purchasePrice,
        supplierName = supplierName,
        variants = variants,
        imageSyncStatus = imageSyncStatus,
        syncStatus = syncStatus,
        deleted = deleted,
        raisedBy = raisedBy,
        updatedBy = updatedBy,
        updatedAt = updatedAt,
        createdAt = createdAt,
        license = license
    )
}
