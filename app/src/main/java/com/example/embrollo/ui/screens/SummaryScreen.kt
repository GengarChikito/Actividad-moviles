package com.example.embrollo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.embrollo.viewmodels.RegistrationViewModel
import com.example.embrollo.model.UserData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    viewModel: RegistrationViewModel,
    onNavigateToHome: () -> Unit
) {
    val user by viewModel.registeredUser.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Resumen de Registro") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "¡Registro Exitoso!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            if (user != null) {
                SummaryItem(label = "Nombre", value = user!!.name)
                SummaryItem(label = "Email", value = user!!.email)
                SummaryItem(label = "Teléfono", value = user!!.phone)
                SummaryItem(label = "Nacimiento", value = user!!.birthDate)
            } else {
                Text("Error: No se encontraron datos de usuario.", color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(onClick = onNavigateToHome) {
                Text("Volver al Inicio")
            }
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label:", fontWeight = FontWeight.SemiBold)
        Text(text = value)
    }
    Divider()
}