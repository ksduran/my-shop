package com.kevinduran.myshop.infrastructure.services.firebase

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseAuthService @Inject constructor(
    private val auth: FirebaseAuth
) {

    fun getCurrentUser() = auth.currentUser

}