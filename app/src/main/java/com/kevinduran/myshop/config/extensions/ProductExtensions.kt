package com.kevinduran.myshop.config.extensions

import com.google.gson.Gson
import com.kevinduran.myshop.domain.models.Product
import com.kevinduran.myshop.infrastructure.model.ProductVariant

fun Product.hasStockControl(): Boolean {
    return try {
        val variants = Gson().fromJson(this.variants, Array<ProductVariant>::class.java)
        variants.any { variant ->
            variant.stock != -1
        }
    } catch (_: Exception) {
        false
    }
}
