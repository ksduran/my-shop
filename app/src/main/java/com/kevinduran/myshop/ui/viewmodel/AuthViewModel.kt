package com.kevinduran.myshop.ui.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.kevinduran.myshop.domain.repositories.EmployeesRepository
import com.kevinduran.myshop.infrastructure.repositories.preferences.UserPreferencesRepository
import com.kevinduran.myshop.infrastructure.services.LicenseService
import com.kevinduran.myshop.infrastructure.services.firebase.FirebaseAuthService
import com.kevinduran.myshop.infrastructure.services.google.GoogleSignInService
import com.kevinduran.myshop.ui.states.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okio.IOException
import javax.inject.Inject

@Suppress("DEPRECATION")
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val googleService: GoogleSignInService,
    private val firebaseAuthService: FirebaseAuthService,
    private val licenseService: LicenseService,
    private val employeesRepository: EmployeesRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthState())
    val uiState = _uiState.asStateFlow()

    init {
        val user = firebaseAuthService.getCurrentUser()
        _uiState.update { it.copy(user = user) }
    }

    suspend fun validateLicense(
        license: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        _uiState.update { it.copy(loading = true) }
        licenseService.validateLicense(
            license = license,
            onSuccess = onSuccess,
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        loading = false,
                        error = error,

                        )
                }
                onFailure()
            }
        )
        _uiState.update { it.copy(loading = false) }
    }

    fun getGoogleSingInIntent() = googleService.getGoogleSingInIntent()

    suspend fun handleSignInResult(data: Intent?) {
        _uiState.update { it.copy(loading = true) }
        googleService.handleSignInResult(data).onSuccess { user ->
            _uiState.update {
                it.copy(
                    user = user,
                    loading = false,
                    error = ""
                )
            }
        }.onFailure { error ->
            _uiState.update {
                it.copy(
                    loading = false,
                    error = "${error.message}"
                )
            }
        }
    }

    fun dismissError() = _uiState.update { it.copy(error = "") }
    suspend fun validateAndSaveUser(
        username: String,
        onSuccess: () -> Unit
    ) {
        try {
            _uiState.update { it.copy(loading = true) }
            val allEmployees = employeesRepository.get().getOrThrow()
            if (allEmployees.isEmpty()) {
                _uiState.update {
                    it.copy(
                        error = "Parece que no hay empleados registrados, comuniquese con el administrador e intente nuevamente",
                        loading = false
                    )
                }
                return
            }
            val user = allEmployees.firstOrNull { it.user == username }
            if (user == null) {
                _uiState.update {
                    it.copy(
                        error = "Parece que el usuario no existe, intente con otro o comuniquese con el administrador.",
                        loading = false
                    )
                }

                return
            }
            preferencesRepository.setUserName(username)
            _uiState.update { it.copy(loading = false) }
            onSuccess()
        } catch (_: IOException) {
            _uiState.update {
                it.copy(
                    error = "Parece que no tienes internet o esta inestable, verifique su conexión a internet e intente nuevamente",
                    loading = false
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    error = "Ha ocurrido un error:\n\n${e.message}",
                    loading = false
                )
            }
        }
    }

}