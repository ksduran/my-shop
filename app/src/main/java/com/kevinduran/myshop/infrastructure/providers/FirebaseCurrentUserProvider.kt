package com.kevinduran.myshop.infrastructure.providers

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kevinduran.myshop.domain.providers.CurrentUserProvider
import javax.inject.Inject

class FirebaseCurrentUserProvider @Inject constructor() : CurrentUserProvider {

    override fun getNameOrEmail(): String {
        val currentUser = Firebase.auth.currentUser
        val userName = currentUser?.displayName ?: currentUser?.email ?: "Unknown"
        return userName
    }

}