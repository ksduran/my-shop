package com.kevinduran.myshop.config.extensions

import com.kevinduran.myshop.domain.models.Sale

fun Sale.toFirebaseMap(): Map<String, Any> {
    return mapOf(
        "uuid" to uuid,
        "productId" to productId,
        "companyName" to companyName,
        "supplierName" to supplierId,
        "color" to color,
        "size" to size,
        "salePrice" to salePrice,
        "purchasePrice" to purchasePrice,
        "raisedBy" to raisedBy,
        "updatedBy" to updatedBy,
        "paymentStatus" to paymentStatus,
        "image" to image,
        "syncStatus" to syncStatus,
        "deleted" to deleted,
        "updatedAt" to updatedAt.toDouble(),
        "createdAt" to createdAt.toDouble()
    )
}
