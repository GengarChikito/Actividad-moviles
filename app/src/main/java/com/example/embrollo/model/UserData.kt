package com.example.embrollo.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val name: String,
    val email: String,
    val phone: String,
    val birthDate: String
)