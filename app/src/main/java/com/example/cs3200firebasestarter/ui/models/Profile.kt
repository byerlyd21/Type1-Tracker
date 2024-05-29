package com.example.cs3200firebasestarter.ui.models

data class Profile (
    val userId: String?,
    val name: String = "",
    val carbsPerUnit: Double = 0.0,
    val adjustmentFactor: Double = 0.0,
)