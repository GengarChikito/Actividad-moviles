package com.example.embrollo.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData1(
    val name: String,
    val email: String,
    val phone: String,
    val birthDate: String
)
//comentario
data class RegistrationUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val birthDate: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    val termsAccepted: Boolean = false,
    val termsError: String? = null,

    val nameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val birthDateError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    val isLoading: Boolean = false,
    val registrationSuccess: Boolean = false,
    val generalError: String? = null
) {
    val isValid: Boolean
        get() = nameError == null &&
                emailError == null &&
                phoneError == null &&
                birthDateError == null &&
                passwordError == null &&
                confirmPasswordError == null &&
                termsError == null &&
                name.isNotBlank() &&
                email.isNotBlank() &&
                phone.isNotBlank() &&
                birthDate.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                termsAccepted
}