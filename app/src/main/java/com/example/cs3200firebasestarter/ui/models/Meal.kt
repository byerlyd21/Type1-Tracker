package com.example.cs3200firebasestarter.ui.models

data class Meal(
    val userId: String = "",
    val restaurant: String = "",
    val description: String = "",
    val carbs: Double = 0.0
)
