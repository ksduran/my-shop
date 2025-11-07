package com.kevinduran.myshop.config.extensions

import java.text.NumberFormat

fun Int.toCurrency() : String {
    return NumberFormat.getCurrencyInstance().format(this)
}