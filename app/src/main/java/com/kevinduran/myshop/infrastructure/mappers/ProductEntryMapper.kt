package com.kevinduran.myshop.infrastructure.mappers

import com.kevinduran.myshop.domain.models.ProductEntry
import com.kevinduran.myshop.infrastructure.request.ProductEntryRequest

fun ProductEntry.toRequest(license: String): ProductEntryRequest {
    return ProductEntryRequest(
        uuid = uuid,
        productId = productId,
        image = image,
        syncStatus = syncStatus,
        deleted = deleted,
        raisedBy = raisedBy,
        updatedBy = updatedBy,
        updatedAt = updatedAt,
        createdAt = createdAt,
        license = license
    )
}
