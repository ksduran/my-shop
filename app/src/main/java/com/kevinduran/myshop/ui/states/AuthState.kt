package com.kevinduran.myshop.ui.states

import com.google.firebase.auth.FirebaseUser

data class AuthState(
    val loading: Boolean = false,
    val error: String = "",
    val user: FirebaseUser? = null
)