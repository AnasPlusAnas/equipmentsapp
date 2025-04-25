package com.example.hkbuequipments

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hkbuequipments.components.CustomTextField
import com.example.hkbuequipments.models.RegisterRequest
import com.example.hkbuequipments.viewmodels.LoginViewModel
import com.example.hkbuequipments.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var remark by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val registerViewModel: RegisterViewModel = viewModel()
    val userId by registerViewModel.userId.collectAsState()
    val loginViewModel: LoginViewModel = viewModel()
    val token by loginViewModel.token.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(userId) {
        Log.d("LaunchedEffect", "userId: $userId")
        if (!userId.isNullOrEmpty()) {
            loginViewModel.login(
                email = email,
                password = password,
                context = context
            )
        }
    }

    LaunchedEffect(token) {
        Log.d("LaunchedEffect", "token: $token")
        if (!token.isNullOrEmpty()) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        CustomTextField(
            keyword = email,
            onKeywordChange = { email = it },
            placeholder = "Email",
            leadingIcon = Icons.Default.MailOutline,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            keyword = password,
            onKeywordChange = { password = it },
            placeholder = "Password",
            leadingIcon = Icons.Default.Lock,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            keyword = contact,
            onKeywordChange = {
                if (it.length <= 8 && it.all { char -> char.isDigit() }) {
                    contact = it
                    errorMessage = if (it.length < 8) "Contact must be exactly 8 digits" else ""
                }
            },
            placeholder = "Contact",
            leadingIcon = Icons.Default.Phone,
            modifier = Modifier.fillMaxWidth(),
            keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            keyword = department,
            onKeywordChange = { department = it },
            placeholder = "Department",
            leadingIcon = Icons.Default.AccountBox,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            keyword = remark,
            onKeywordChange = { remark = it },
            placeholder = "Remark",
            leadingIcon = Icons.Default.Create,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty() && contact.isNotEmpty()) {
                    val registerRequest = RegisterRequest(
                        email = email,
                        password = password,
                        contact = contact,
                        department = department,
                        remark = remark
                    )
                    registerViewModel.register(registerRequest)
                } else {
                    errorMessage = "Email, password, and contact are required"
                }
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
                "Register",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}