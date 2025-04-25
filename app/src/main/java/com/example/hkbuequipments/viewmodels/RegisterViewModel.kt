package com.example.hkbuequipments.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hkbuequipments.DataStoreInstance
import com.example.hkbuequipments.KtorClient
import com.example.hkbuequipments.models.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _userId = MutableStateFlow<String>("")
    val userId: StateFlow<String?> = _userId

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            try {
                val resultId = KtorClient.register(request)
                if (resultId.isNotEmpty()) {
                    _userId.value = resultId
                } else {
                    Log.e("RegisterViewModel", "Registration failed: User ID is empty")
                }
            } catch (e: Exception) {
                Log.e("RegisterViewModel", "Login error: ${e.message}")
            }
        }
    }
}