package com.kevinduran.myshop.infrastructure.mappers

import com.google.gson.Gson
import com.kevinduran.myshop.domain.models.SaleReturn
import com.kevinduran.myshop.domain.models.SaleReturnProduct
import com.kevinduran.myshop.infrastructure.request.SaleReturnRequest

fun SaleReturn.toRequest(license: String): SaleReturnRequest {
    return SaleReturnRequest(
        uuid = uuid,
        storeName = storeName,
        imagePath = imagePath,
        products = Gson().toJson(products),
        imageSyncStatus = imageSyncStatus,
        syncStatus = syncStatus,
        deleted = deleted,
        updatedAt = updatedAt,
        createdAt = createdAt,
        license = license
    )
}

fun SaleReturnRequest.toModel(): SaleReturn {
    return SaleReturn(
        uuid = this.uuid,
        storeName = this.storeName,
        imagePath = this.imagePath,
        products = Gson().fromJson(this.products, Array<SaleReturnProduct>::class.java).toList(),
        imageSyncStatus = this.imageSyncStatus,
        syncStatus = this.syncStatus,
        deleted = this.deleted,
        updatedAt = this.updatedAt,
        createdAt = this.createdAt
    )
}
