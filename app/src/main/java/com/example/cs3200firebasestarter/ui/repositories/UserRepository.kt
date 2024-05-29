package com.example.cs3200firebasestarter.ui.repositories

import android.util.Log
import com.example.cs3200firebasestarter.ui.models.Profile
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object UserRepository {
    suspend fun createUser(email: String, password: String) {
        try {
            Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            Log.d("UserRepository", "User created successfully: ${getCurrentUserId()}")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error creating user: ${e.message}")
        }
    }

    suspend fun loginUser(email: String, password: String) {
        try {
            Firebase.auth.signInWithEmailAndPassword(email, password).await()
            Log.d("UserRepository", "User logged in successfully: ${getCurrentUserId()}")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error logging in user: ${e.message}")
        }
    }

    fun getCurrentUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    fun logout() {
        Firebase.auth.signOut()
    }
}