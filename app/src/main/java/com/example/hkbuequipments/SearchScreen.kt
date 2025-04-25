package com.example.hkbuequipments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hkbuequipments.components.CustomTextField
import com.example.hkbuequipments.components.EquipmentCard
import com.example.hkbuequipments.components.PaginatedLazyColumn
import com.example.hkbuequipments.viewmodels.EquipmentsViewModel

@Composable
fun SearchScreen(navController: NavController) {
    val viewModel: EquipmentsViewModel = viewModel()
    val equipments = viewModel.equipmentList.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val currentPage = viewModel.currentPage.collectAsStateWithLifecycle().value

    var keyword by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadEquipments(keyword = keyword, page = currentPage)
    }

    // add a search bar to the top of the screen
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        CustomTextField(
            keyword = keyword,
            onKeywordChange = { newKeyword ->
                keyword = newKeyword
                viewModel.loadEquipments(keyword = newKeyword, page = currentPage)
            },
            placeholder = "Search",
            leadingIcon = Icons.Default.Search,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp, vertical = 8.dp),
        )

        PaginatedLazyColumn(
            items = equipments,
            isLoading = isLoading,
            currentPage = currentPage,
            onNextPage = { viewModel.nextPage(keyword) },
            onPrevPage = { viewModel.prevPage(keyword) },
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
}