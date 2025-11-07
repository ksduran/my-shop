package com.kevinduran.myshop.ui.states

data class ReportsState(
    val license: String = "0",
    val loading: Boolean = false,
    val error: String = ""
)