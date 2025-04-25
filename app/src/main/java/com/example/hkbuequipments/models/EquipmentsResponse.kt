package com.example.hkbuequipments.models

import kotlinx.serialization.Serializable

@Serializable
data class EquipmentsResponse(
    val equipments: List<Equipment>,
    val total: Int,
    val page: Int,
    val perPage : Int,
)