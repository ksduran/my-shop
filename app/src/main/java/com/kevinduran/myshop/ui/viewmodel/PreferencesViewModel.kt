package com.kevinduran.myshop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import com.kevinduran.myshop.ui.states.PreferencesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferences: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PreferencesState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val state = PreferencesState(
                isAdmin = preferences.getIsAdmin().first(),
                license = preferences.getLicenseId().first()
            )
            _uiState.value = state
        }
    }

    suspend fun getLicense() = preferences.getLicenseId().first()
    suspend fun addLicense(value: Long) = preferences.setLicenseId(value)

    suspend fun getIsAdmin() = preferences.getIsAdmin().first()
    suspend fun addIsAdmin(value: Boolean) = preferences.setIsAdmin(value)

}