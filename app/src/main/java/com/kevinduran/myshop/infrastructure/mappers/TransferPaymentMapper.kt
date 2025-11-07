package com.kevinduran.myshop.infrastructure.mappers


import com.google.gson.Gson
import com.kevinduran.myshop.domain.models.TransferPayment
import com.kevinduran.myshop.domain.models.TransferPaymentProduct


import com.kevinduran.myshop.infrastructure.request.TransferPaymentRequest

fun TransferPayment.toRequest(license: String): TransferPaymentRequest {
    return TransferPaymentRequest(
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

fun TransferPaymentRequest.toModel(): TransferPayment {
    return TransferPayment(
        uuid = this.uuid,
        license = this.license,
        storeName = this.storeName,
        imagePath = this.imagePath,
        products = Gson().fromJson(this.products, Array<TransferPaymentProduct>::class.java)
            .toList(),
        imageSyncStatus = this.imageSyncStatus,
        syncStatus = this.syncStatus,
        deleted = this.deleted,
        updatedAt = this.updatedAt,
        createdAt = this.createdAt
    )
}
