package com.example.embrollo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.embrollo.viewmodels.RegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel,
    onRegistrationSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.registrationSuccess) {
        if (uiState.registrationSuccess) {
            onRegistrationSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Registro de Usuario",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface // Color de fondo del Scaffold
    ) { innerPadding ->
        // Usa Surface para aplicar el color de fondo del tema al contenido
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                RegistrationTextField(
                    value = uiState.name,
                    onValueChange = viewModel::updateName,
                    label = "Nombre Completo",
                    error = uiState.nameError,
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                )

                RegistrationTextField(
                    value = uiState.email,
                    onValueChange = viewModel::updateEmail,
                    label = "Correo Electrónico",
                    error = uiState.emailError,
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
                )

                RegistrationTextField(
                    value = uiState.phone,
                    onValueChange = viewModel::updatePhone,
                    label = "Teléfono (9 dígitos)",
                    error = uiState.phoneError,
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
                )

                RegistrationTextField(
                    value = uiState.birthDate,
                    onValueChange = viewModel::updateBirthDate,
                    label = "Fecha de Nacimiento (DD/MM/AAAA)",
                    error = uiState.birthDateError,
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
                )

                RegistrationTextField(
                    value = uiState.password,
                    onValueChange = viewModel::updatePassword,
                    label = "Contraseña",
                    error = uiState.passwordError,
                    isPassword = true,
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }
                )

                RegistrationTextField(
                    value = uiState.confirmPassword,
                    onValueChange = viewModel::updateConfirmPassword,
                    label = "Confirmar Contraseña",
                    error = uiState.confirmPasswordError,
                    isPassword = true,
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }
                )

                // Checkbox para términos y condiciones
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.termsAccepted,
                        onCheckedChange = viewModel::updateTermsAccepted
                    )
                    Text(
                        text = "Acepto los términos y condiciones.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    )
                }

                uiState.termsError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, bottom = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                uiState.generalError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                Button(
                    onClick = viewModel::registerUser,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Registrar", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

// Componente reutilizable para campos de texto con error
@Composable
fun RegistrationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    isPassword: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        // Muestra el mensaje de error con supportingText
        supportingText = if (error != null) {
            { Text(text = error, color = MaterialTheme.colorScheme.error) }
        } else null,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        leadingIcon = leadingIcon,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}