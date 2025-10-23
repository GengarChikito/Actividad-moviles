package com.example.embrollo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.embrollo.model.RegistrationUiState
import com.example.embrollo.model.UserData
import com.example.embrollo.navigation.NavigationEvent
import com.example.embrollo.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val _navigationEvents = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvents = _navigationEvents.asStateFlow()

    private val _registeredUser = MutableStateFlow<UserData?>(null)
    val registeredUser: StateFlow<UserData?> = _registeredUser.asStateFlow()

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name, nameError = null) }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    fun updatePhone(phone: String) {
        _uiState.update { it.copy(phone = phone, phoneError = null) }
    }

    fun updateBirthDate(date: String) {
        _uiState.update { it.copy(birthDate = date, birthDateError = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, confirmPasswordError = null) }
    }

    fun updateTermsAccepted(accepted: Boolean) {
        _uiState.update { it.copy(termsAccepted = accepted, termsError = null) } // Checkbox update xD
    }

    private fun validateFields(): Boolean {
        val currentState = _uiState.value

        _uiState.update { it.copy(generalError = null) }

        var hasError = false
        var newErrors = currentState.copy(
            nameError = null, emailError = null, phoneError = null,
            birthDateError = null, passwordError = null, confirmPasswordError = null,
            termsError = null
        )

        if (currentState.name.isBlank()) {
            newErrors = newErrors.copy(nameError = "El nombre es obligatorio")
            hasError = true
        }

        if (!isValidEmail(currentState.email)) {
            newErrors = newErrors.copy(emailError = "Formato de email incorrecto")
            hasError = true
        }

        if (!isValidPhone(currentState.phone)) {
            newErrors = newErrors.copy(phoneError = "Teléfono inválido (ej: 9 dígitos)")
            hasError = true
        }

        if (currentState.birthDate.isBlank()) {
            newErrors = newErrors.copy(birthDateError = "La fecha de nacimiento es obligatoria")
            hasError = true
        }

        if (currentState.password.length < 6) {
            newErrors = newErrors.copy(passwordError = "La contraseña debe tener al menos 6 caracteres")
            hasError = true
        }

        if (currentState.password != currentState.confirmPassword) {
            newErrors = newErrors.copy(confirmPasswordError = "Las contraseñas no coinciden")
            hasError = true
        }

        if (!currentState.termsAccepted) {
            newErrors = newErrors.copy(termsError = "Debes aceptar los términos y condiciones.")
            hasError = true
        }

        _uiState.update { newErrors }
        return !hasError
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}"))
    }

    private fun isValidPhone(phone: String): Boolean {
        return phone.matches(Regex("\\d{9}"))
    }

    fun registerUser() {
        if (!validateFields()) {
            _uiState.update { it.copy(generalError = "Por favor, corrige los errores del formulario.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            try {
                delay(2000) // Simulación de carga xD

                val user = UserData(
                    name = _uiState.value.name,
                    email = _uiState.value.email,
                    phone = _uiState.value.phone,
                    birthDate = _uiState.value.birthDate
                )

                _registeredUser.value = user
                _uiState.update { it.copy(isLoading = false, registrationSuccess = true) }

                _navigationEvents.value = NavigationEvent.NavigateTo(route = Screen.Summary)

            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    generalError = "Error en el registro: Intenta de nuevo. (${e.localizedMessage ?: "Desconocido"})"
                ) }
            }
        }
    }
//coment
    fun navigationEventHandled() {
        _navigationEvents.value = null
    }
}