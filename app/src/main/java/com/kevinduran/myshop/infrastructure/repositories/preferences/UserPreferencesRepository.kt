package com.kevinduran.myshop.infrastructure.repositories.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "settings")

class UserPreferencesRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    private object PreferencesKeys {
        val LICENSE_ID = longPreferencesKey("license_id")
        val IS_ADMIN = booleanPreferencesKey("is_admin")
        val USER_NAME = stringPreferencesKey("user_name")
    }

    fun getLicenseId(): Flow<Long> {
        return context.dataStore.data.map { prefs ->
            prefs[PreferencesKeys.LICENSE_ID] ?: 0L
        }
    }

    suspend fun setLicenseId(value: Long) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.LICENSE_ID] = value
        }
    }

    fun getIsAdmin(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[PreferencesKeys.IS_ADMIN] ?: false
        }
    }

    suspend fun setIsAdmin(value: Boolean) {
        context.dataStore.edit {
            it[PreferencesKeys.IS_ADMIN] = value
        }
    }

    fun getUserName(): Flow<String> {
        return context.dataStore.data.map {
            it[PreferencesKeys.USER_NAME] ?: ""
        }
    }

    suspend fun setUserName(value: String) {
        context.dataStore.edit {
            it[PreferencesKeys.USER_NAME] = value
        }
    }


}