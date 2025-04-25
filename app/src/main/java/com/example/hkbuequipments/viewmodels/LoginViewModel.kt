package com.example.hkbuequipments.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hkbuequipments.DataStoreInstance
import com.example.hkbuequipments.KtorClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login(context: Context, email: String, password: String) {
        viewModelScope.launch {
            try {
                val resultToken = KtorClient.login(email, password)
                if (resultToken.isNotEmpty()) {
                    _token.value = resultToken
                    _errorMessage.value = null
                    DataStoreInstance.saveStringPreferences(context, DataStoreInstance.TOKEN_KEY, resultToken)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Login error: ${e.message}"
            }
        }
    }

    // Store application context in the ViewModel
    private var applicationContext: Context? = null

    fun setApplicationContext(context: Context) {
        applicationContext = context.applicationContext
    }

    fun setupLogoutHandler() {
        KtorClient.setLogoutCallback {
            // Run this on the main thread
            viewModelScope.launch {
                applicationContext?.let { ctx ->
                    logout(ctx)
                }
            }
        }
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            _token.value = null
            DataStoreInstance.saveStringPreferences(context, DataStoreInstance.TOKEN_KEY, "")
        }
    }

    fun loadToken(context: Context) {
        viewModelScope.launch {
            DataStoreInstance.getStringPreferences(context, DataStoreInstance.TOKEN_KEY).collect { token ->
                _token.value = token
            }
        }
    }
}