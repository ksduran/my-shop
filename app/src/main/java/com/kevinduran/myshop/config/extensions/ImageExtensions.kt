package com.kevinduran.myshop.config.extensions

import android.content.Context
import android.net.Uri
import java.io.File

/**
 * Convert a stored file name into a [Uri] pointing to the file inside the
 * app's internal storage.
 */
fun String.toInternalImageUri(context: Context): Uri {
    return Uri.fromFile(File(context.filesDir, this))
}

fun Uri.toInternalImageUri(context: Context): Uri {
    return Uri.fromFile(File(context.filesDir, this.path))
}

fun String.generateProductImageFullPath(license: String): String {
    val fileName = this.split("/").last()
    return "https://keranstore.com/api/borrowed/files/$license/products/$fileName"
}

fun String.generatePaymentsImageFullPath(license: String): String {
    val fileName = this.split("/").last()
    return "https://keranstore.com/api/borrowed/files/$license/payments/$fileName"
}

fun String.generateReturnsImageFullPath(license: String): String {
    val fileName = this.split("/").last()
    return "https://keranstore.com/api/borrowed/files/$license/returns/$fileName"
}

