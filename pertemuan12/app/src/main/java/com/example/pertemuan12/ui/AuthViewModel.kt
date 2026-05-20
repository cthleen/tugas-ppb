package com.example.pertemuan12.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pertemuan12.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthResult {
    object Idle : AuthResult()
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    // --- Login State ---
    private val _loginEmail = MutableStateFlow("")
    val loginEmail: StateFlow<String> = _loginEmail.asStateFlow()

    private val _loginPassword = MutableStateFlow("")
    val loginPassword: StateFlow<String> = _loginPassword.asStateFlow()

    private val _loginResult = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val loginResult: StateFlow<AuthResult> = _loginResult.asStateFlow()

    // --- Register State ---
    private val _registerEmail = MutableStateFlow("")
    val registerEmail: StateFlow<String> = _registerEmail.asStateFlow()

    private val _registerPassword = MutableStateFlow("")
    val registerPassword: StateFlow<String> = _registerPassword.asStateFlow()

    private val _registerResult = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val registerResult: StateFlow<AuthResult> = _registerResult.asStateFlow()

    // --- Login Actions ---
    fun onLoginEmailChange(value: String) { _loginEmail.value = value }
    fun onLoginPasswordChange(value: String) { _loginPassword.value = value }

    fun onLogin() {
        val email = _loginEmail.value.trim()
        val password = _loginPassword.value.trim()

        if (email.isBlank() || password.isBlank()) {
            _loginResult.value = AuthResult.Error("Email dan password tidak boleh kosong!")
            return
        }

        viewModelScope.launch {
            val user = repository.login(email, password)
            _loginResult.value = if (user != null) {
                AuthResult.Success
            } else {
                AuthResult.Error("Email atau password salah!")
            }
        }
    }

    fun resetLoginResult() { _loginResult.value = AuthResult.Idle }

    // --- Register Actions ---
    fun onRegisterEmailChange(value: String) { _registerEmail.value = value }
    fun onRegisterPasswordChange(value: String) { _registerPassword.value = value }

    fun onRegister() {
        val email = _registerEmail.value.trim()
        val password = _registerPassword.value.trim()

        if (email.isBlank() || password.isBlank()) {
            _registerResult.value = AuthResult.Error("Email dan password tidak boleh kosong!")
            return
        }

        if (password.length < 6) {
            _registerResult.value = AuthResult.Error("Password minimal 6 karakter!")
            return
        }

        viewModelScope.launch {
            val existing = repository.getUserByEmail(email)
            if (existing != null) {
                _registerResult.value = AuthResult.Error("Email sudah terdaftar!")
            } else {
                repository.register(email, password)
                _registerResult.value = AuthResult.Success
            }
        }
    }

    fun resetRegisterResult() { _registerResult.value = AuthResult.Idle }
}

class AuthViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
