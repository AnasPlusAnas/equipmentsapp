package com.example.hkbuequipments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hkbuequipments.components.EquipmentCard
import com.example.hkbuequipments.components.PaginatedLazyColumn
import com.example.hkbuequipments.viewmodels.EquipmentsViewModel

@Composable
fun LocationEquipmentsScreen(navController: NavController, location: String) {
    val viewModel: EquipmentsViewModel = viewModel()
    val equipments = viewModel.equipmentList.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.loadEquipments(location = location)
    }

    PaginatedLazyColumn(
        items = equipments,
        isLoading = isLoading,
        currentPage = viewModel.currentPage.collectAsStateWithLifecycle().value,
        onNextPage = { viewModel.nextPage(location = location) },
        onPrevPage = { viewModel.prevPage(location = location) },
        key = { it.id }
    ) { equipment ->
        EquipmentCard(
            imageUrl = equipment.image,
            title = equipment.name,
            location = equipment.location,
            detail = equipment.description,
            color = equipment.color,
            onClick = {
                navController.navigate("equipmentDetail/${equipment.id}/false")
            },
        )
    }
}