package com.example.studentmanagementapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentmanagementapp.data.remote.AuthApi
import com.example.studentmanagementapp.data.remote.TokenStorage

class LoginViewModelFactory(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(authApi, tokenStorage) as T
    }
}
