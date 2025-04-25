package com.example.hkbuequipments.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("_id")
    val id: String = "",
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val contact: String = "",
    val department: String = "",
    val remark: String = "",
    val isAdmin: Boolean = false,
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("modified_at")
    val modifiedAt: String = "",
    val tokens: List<String> = emptyList(),
    val equipments: List<Equipment> = emptyList()
)