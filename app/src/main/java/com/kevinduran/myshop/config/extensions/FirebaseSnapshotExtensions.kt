package com.kevinduran.myshop.config.extensions

import com.google.firebase.database.DataSnapshot

fun DataSnapshot.getUpdatedAtAsLong(): Long {
    return when (val value = this.child("updatedAt").value) {
        is Long -> value
        is Double -> value.toLong()
        is String -> value.toLongOrNull() ?: System.currentTimeMillis()
        else -> System.currentTimeMillis()
    }
}