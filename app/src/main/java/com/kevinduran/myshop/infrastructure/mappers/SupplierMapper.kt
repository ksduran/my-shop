package com.kevinduran.myshop.infrastructure.mappers

import com.kevinduran.myshop.domain.models.Supplier
import com.kevinduran.myshop.infrastructure.request.SupplierRequest

fun Supplier.toRequest(license: String): SupplierRequest {
    return SupplierRequest(
        uuid = uuid,
        name = name,
        debtControl = debtControl,
        syncStatus = syncStatus,
        deleted = deleted,
        raisedBy = raisedBy,
        updatedBy = updatedBy,
        updatedAt = updatedAt,
        createdAt = createdAt,
        license = license
    )
}
