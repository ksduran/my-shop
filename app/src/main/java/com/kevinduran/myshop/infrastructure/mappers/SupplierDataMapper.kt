package com.kevinduran.myshop.infrastructure.mappers

import com.kevinduran.myshop.domain.models.SupplierData
import com.kevinduran.myshop.infrastructure.request.SupplierDataRequest

fun SupplierData.toRequest(license: String): SupplierDataRequest {
    return SupplierDataRequest(
        uuid = uuid,
        supplierId = supplierId,
        productId = productId,
        title = title,
        quantity = quantity,
        image = image,
        purchasePrice = purchasePrice,
        type = type,
        syncStatus = syncStatus,
        deleted = deleted,
        raisedBy = raisedBy,
        updatedBy = updatedBy,
        updatedAt = updatedAt,
        createdAt = createdAt,
        license = license
    )
}
