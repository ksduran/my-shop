package com.kevinduran.myshop.infrastructure.services

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LicenseService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseDatabase: FirebaseDatabase,
    private val preferences: UserPreferencesRepository
) {

    suspend fun validateLicense(
        license: String,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        val snapshot = firebaseDatabase.getReference("licenses")
            .get()
            .await()
        if (!snapshot.exists()) return onFailure("No hay licencias disponibles, verifique su internet e intent de nuevo")
        var packageMatch = false
        for (child in snapshot.children) {
            val licenseValue = child.getValue(Long::class.java)
            if (licenseValue == (license.toLongOrNull() ?: 0)) {
                preferences.setLicenseId(license.toLong())
                packageMatch = true
                onSuccess()
                break
            }
        }
        if (!packageMatch) {
            onFailure("Su licencia no tiene acceso o ha sido suspendida.")
        }
    }

}