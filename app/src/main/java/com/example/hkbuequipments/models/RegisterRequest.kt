package com.example.hkbuequipments.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val contact: String,
    val department: String,
    val remark: String,
    val isAdmin: Boolean = false,
)