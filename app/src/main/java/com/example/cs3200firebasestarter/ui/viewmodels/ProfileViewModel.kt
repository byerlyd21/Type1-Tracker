package com.example.cs3200firebasestarter.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cs3200firebasestarter.ui.models.Profile
import com.example.cs3200firebasestarter.ui.repositories.ProfileRepository
import com.example.cs3200firebasestarter.ui.repositories.UserRepository
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application): AndroidViewModel(application) {

    private val _profile = MutableLiveData<Profile?>()
    val profile: LiveData<Profile?> = _profile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val profileData = ProfileRepository.getProfileData() ?: createEmptyProfile()
                Log.d("ProfileViewModel", "getting profile data: $profileData")
                _profile.value = profileData
            } catch (e: Exception) {
                Log.e("ProfileViewModel.kt", e.message ?: "No message available")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun createEmptyProfile() : Profile {
        val profile = Profile(
            name = "",
            carbsPerUnit = 0.0,
            adjustmentFactor = 0.0,
            userId = UserRepository.getCurrentUserId()
        )
        Log.d("ProfileViewModel", "getProfileData was null")
        return profile
    }

    fun updateProfileData( updatedProfile: Profile) {
        viewModelScope.launch {
            try {
                Log.d("ProfileViewModel", "updateProfileData called with: $updatedProfile")
                ProfileRepository.updateProfileData(updatedProfile) // Replace with actual update logic
                loadProfileData() // Refresh user data
            } catch (e: Exception) {
                Log.e("ProfileViewModel.kt", e.message ?: "No message available")
            }
        }
    }
}


