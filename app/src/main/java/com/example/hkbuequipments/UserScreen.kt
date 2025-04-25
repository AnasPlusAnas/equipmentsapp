package com.example.hkbuequipments

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hkbuequipments.components.CustomTextField
import com.example.hkbuequipments.components.EquipmentCard
import com.example.hkbuequipments.models.Equipment
import com.example.hkbuequipments.viewmodels.EquipmentsViewModel
import com.example.hkbuequipments.viewmodels.LoginViewModel
import kotlin.random.Random

@Composable
fun UserScreen(navController: NavController?) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val loginViewModel: LoginViewModel = viewModel()
    val equipmentsViewModel: EquipmentsViewModel = viewModel()
    val equipments: List<Equipment> = equipmentsViewModel.equipmentList.collectAsState().value
    val isLoading = equipmentsViewModel.isLoading.collectAsState().value
    val token = loginViewModel.token.collectAsState().value
    val context = LocalContext.current

    // Set application context when the composable is first created
    LaunchedEffect(Unit) {
        loginViewModel.setApplicationContext(context)
        loginViewModel.setupLogoutHandler()
        loginViewModel.loadToken(context)
    }

    LaunchedEffect(token) {
        Log.d("UserScreen", "Token: $token")
        if (!token.isNullOrEmpty()) {
            Log.d("UserScreen", "Token is not null or empty")
            equipmentsViewModel.getUserEquipments()
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 16.dp, start = 16.dp)
    ) {
        if (token.isNullOrEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                ) {
                    CustomTextField(
                        keyword = email.value,
                        onKeywordChange = { email.value = it },
                        placeholder = "Email",
                        leadingIcon = Icons.Default.MailOutline,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    CustomTextField(
                        keyword = password.value,
                        onKeywordChange = { password.value = it },
                        placeholder = "Password",
                        leadingIcon = Icons.Default.Lock,
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.size(24.dp))

                    // Login
                    Button(
                        onClick = {
                            loginViewModel.login(context, email.value, password.value)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            "Login",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.size(12.dp))

                    // Register
                    Button(
                        onClick = {
                            navController?.navigate("register")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            "Register",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { loginViewModel.logout(context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 8.dp, end = 8.dp)
                        .height(58.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("Logout", style = MaterialTheme.typography.titleLarge)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp, start = 12.dp, top = 16.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(48.dp),
                            strokeWidth = 4.dp
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                    ),
                                ) {
                                    Text(
                                        text = "My Equipments",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.White,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                            items(
                                items = equipments,
                                key = { it.id + Random.nextInt(0, 1000) }
                            ) { item ->
                                EquipmentCard(
                                    imageUrl = item.image,
                                    title = item.name,
                                    location = item.location,
                                    detail = item.description,
                                    color = item.color,
                                    onClick = {
                                        Log.d("UserScreen", "Clicked on item: ${item}")
                                        navController?.navigate("equipmentDetail/${item.id}/true")
                                    }
                                )
                                Spacer(modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
@Preview(showBackground = true)
fun UserScreenPreview() {
    UserScreen(null)
}