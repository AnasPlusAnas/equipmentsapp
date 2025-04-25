package com.example.hkbuequipments

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.hkbuequipments.viewmodels.EquipmentsViewModel
import com.example.hkbuequipments.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun EquipmentDetailScreen(
    snackbarHostState: SnackbarHostState,
    id: String,
    isRented: String?
) {
    val viewModel: EquipmentsViewModel = viewModel()
    val equipment = viewModel.equipmentDetail.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val errorMsg = viewModel.errorMessage.collectAsStateWithLifecycle().value
    val reserveResult = viewModel.reserveResult.collectAsStateWithLifecycle().value

    val isActuallyRented = remember { mutableStateOf(isRented?.toBoolean() ?: false) }

    val loginViewModel: LoginViewModel = viewModel()
    val token = loginViewModel.token.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    fun handleReservationResult(result: Boolean) {
        if (result) {
            // Toggle the rental state
            isActuallyRented.value = !isActuallyRented.value

            // Show snackbar
            coroutineScope.launch {
                val msg = if (isActuallyRented.value) "Reserved" else "Unreserved"
                snackbarHostState.showSnackbar(
                    message = "Equipment $msg successfully",
                    withDismissAction = true,
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadEquipmentById(id)
        loginViewModel.loadToken(context)
        Log.d("EquipmentDetailScreen", "isRented: $isRented")
        // Add result collector here
        viewModel.reserveResult.collect { result ->
            if (result == true) {
                handleReservationResult(true)
                viewModel.resetReserveResult()
            }
        }
    }

    LaunchedEffect(errorMsg) {
        if (!errorMsg.isNullOrEmpty()) {
            snackbarHostState.showSnackbar(
                message = errorMsg,
                withDismissAction = true,
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                )
            } else {
                val equipmentColorString = equipment.color
                val equipmentColor = if (equipmentColorString.isNotEmpty()) {
                    Color(android.graphics.Color.parseColor(equipmentColorString))
                } else {
                    Color.Gray // Default color if equipment.color is null or empty
                }
                val gradientColors = listOf(
                    equipmentColor.copy(alpha = 0.2f),
                    equipmentColor.copy(alpha = 0.1f),
                    Color.Transparent
                )
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(gradientColors),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    AsyncImage(
                        model = equipment.image,
                        contentDescription = "Equipment Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Text(
                        text = equipment.name,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            DetailItem("Contact Person", equipment.contactPerson)
                            HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.3f))
                            DetailItem("Description", equipment.description)
                            HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.3f))
                            DetailItem("Location", equipment.location)
                            HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.3f))
                            DetailItem("Color", equipment.color)
                            HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.3f))
                            DetailItem("Created At", equipment.createdAt)
                            HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.3f))
                            DetailItem("Modified At", equipment.modifiedAt)
                        }
                    }

                    if (!token.isNullOrEmpty()) {
                        Button(
                            onClick = {
                                if (isActuallyRented.value) {
                                    viewModel.unreserveEquipment(equipment.id)
                                } else {
                                    viewModel.reserveEquipment(equipment.id)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp, bottom = 16.dp)
                                .height(60.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isActuallyRented.value) 
                                    Color.Red.copy(alpha = 0.8f) else MaterialTheme.colorScheme.primary
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                        ) {
                            Text(
                                if (isActuallyRented.value) "Unreserve" else "Reserve",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}