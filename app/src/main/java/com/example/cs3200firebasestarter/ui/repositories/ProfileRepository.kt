package com.example.cs3200firebasestarter.ui.repositories

import android.util.Log
import com.example.cs3200firebasestarter.ui.models.Profile
import com.example.cs3200firebasestarter.ui.repositories.UserRepository.getCurrentUserId
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object ProfileRepository {
    private val db = Firebase.firestore

    suspend fun getProfileData(): Profile? {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val snapshot = db.collection("users").document(userId).get().await()
                if (snapshot.exists()) {
                    val data = snapshot.data
                    if (data != null) {
                        return Profile(
                            userId = data["userId"] as String? ?: "",
                            name = data["name"] as String? ?: "",
                            carbsPerUnit = data["carbsPerUnit"] as Double? ?: 0.0,
                            adjustmentFactor = data["adjustmentFactor"] as Double? ?: 0.0
                        )
                    }
                }
            }
            return null
        } catch (e: Exception) {
            Log.e("ProfileRepository.kt", e.message ?: "No message available")
            return null
        }
    }

    suspend fun updateProfileData(
        updatedProfile: Profile) {
        try {
            Log.d("ProfileRepository", "updateProfileData called with: $updatedProfile")
            val userId = updatedProfile.userId
            if (userId != null) {
                db.collection("users").document(userId).set(updatedProfile, SetOptions.merge()).await()
            }
        } catch (e: Exception) {
            Log.e("ProfileRepository.kt", e.message ?: "No message available")
        }
    }

    suspend fun getCarbsPerUnit(): Double? {
        return try {
            val userId = getCurrentUserId()
            userId?.let {
                val snapshot = db.collection("users").document(it).get().await()
                snapshot.getDouble("carbsPerUnit")
            }
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Error fetching carbsPerUnit", e)
            null
        }
    }

    suspend fun getAdjustmentFactor(): Double? {
        return try {
            val userId = getCurrentUserId()
            userId?.let {
                val snapshot = db.collection("users").document(it).get().await()
                snapshot.getDouble("adjustmentFactor")
            }
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Error fetching carbsPerUnit", e)
            null
        }
    }

}