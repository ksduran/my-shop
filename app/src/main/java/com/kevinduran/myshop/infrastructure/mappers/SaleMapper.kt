package com.kevinduran.myshop.infrastructure.mappers

import com.kevinduran.myshop.domain.models.Sale
import com.kevinduran.myshop.infrastructure.request.SaleRequest

fun Sale.toRequest(license: String): SaleRequest {
    return SaleRequest(
        uuid = uuid,
        productId = productId,
        companyName = companyName,
        supplierId = supplierId,
        color = color,
        size = size,
        sizeR = sizeR,
        changedProductId = changedProductId,
        changedSize = changedSize,
        changedProductColor = changedProductColor,
        salePrice = salePrice,
        purchasePrice = purchasePrice,
        raisedBy = raisedBy,
        updatedBy = updatedBy,
        paymentStatus = paymentStatus,
        image = image,
        syncStatus = syncStatus,
        deleted = deleted,
        updatedAt = updatedAt,
        createdAt = createdAt,
        license = license
    )
}
