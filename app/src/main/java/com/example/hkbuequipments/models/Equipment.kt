package com.example.hkbuequipments.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Equipment(
    @SerialName("_id")
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val description: String = "",
    val image: String = "",
    @SerialName("contact_person")
    val contactPerson: String = "",
    val color: String = "",
    val highlight: Boolean = false,
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("modified_at")
    val modifiedAt: String = "",
    val rented: Boolean = false,
)