package com.kevinduran.myshop.ui.states

data class PreferencesState(
    val isAdmin: Boolean = false,
    val license: Long = 0L
)