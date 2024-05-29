package com.example.cs3200firebasestarter.ui.repositories

import android.util.Log
import com.example.cs3200firebasestarter.ui.models.Meal
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object MealRepository {
    private val db = Firebase.firestore

    suspend fun getMeals(): List<Meal> {
        return try {
            val snapshot = db.collection("meals").get().await()
            snapshot.documents.mapNotNull { document ->
                //map the document fields to a Meal object
                Meal(
                    userId = document.getString("userId") ?: "",
                    restaurant = document.getString("restaurant") ?: "",
                    description = document.getString("description") ?: "",
                    carbs = document.getDouble("carbs") ?: 0.0
                )
            }
        } catch (e: Exception) {
            Log.e("MealRepository.kt", e.message ?: "No message available")
            emptyList() // Return an empty list in case of failure
        }
    }

    suspend fun createMeal(
        resturant: String,
        description: String,
        carbs: Double
    ): Meal? {
        val userId = UserRepository.getCurrentUserId()
        if (userId != null) {
            val doc = db.collection("meals").document()

            val meal = Meal(
                restaurant = resturant,
                description = description,
                carbs = carbs,
                userId = userId
            )
            try {
                doc.set(meal).await()
                Log.d("MealRepository.kt", "Meal added successfully with ID: ${doc.id}")
            } catch (e: Exception) {
                Log.e("MealRepository.kt", e.message ?: "No message available")
            }
            return meal
        }
        return null
    }
}