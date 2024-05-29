package com.example.cs3200firebasestarter.ui.viewmodels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cs3200firebasestarter.ui.models.Meal
import com.example.cs3200firebasestarter.ui.repositories.MealRepository
import com.example.cs3200firebasestarter.ui.repositories.ProfileRepository
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.round
import java.time.LocalTime

class MealsViewModel(application: Application): AndroidViewModel(application) {
    private val _carbsPerUnit = MutableLiveData<Double>()

    private val _adjFactor = MutableLiveData<Double>()

    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> = _meals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    val mealCreationStatus = MutableLiveData<Boolean>()


    init {
        loadMeals()
        loadProfileData()
    }

    private fun loadMeals() {
        viewModelScope.launch {
            _meals.value = MealRepository.getMeals()
            _isLoading.value = true
            try {
                _meals.value = MealRepository.getMeals()
            } catch (e: Exception) {
                Log.e("MealsViewModel.kt", e.message ?: "No message available")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createMeal(restaurant: String, description: String, carbs: Double) {
        viewModelScope.launch {
            try {
                val newMeal = MealRepository.createMeal(restaurant, description, carbs)
                if (newMeal != null) {
                    val updatedMeals = _meals.value.orEmpty() + newMeal
                    _meals.value = updatedMeals
                }
                mealCreationStatus.value = true
            } catch (e: Exception) {
                Log.e("MealsViewModel", "Error creating meal", e)
                mealCreationStatus.value = false
            }
        }
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            val carbs = ProfileRepository.getCarbsPerUnit() ?: 0.0
            _carbsPerUnit.value = carbs
            val adjFactor = ProfileRepository.getAdjustmentFactor() ?: 0.0
            _adjFactor.value = adjFactor
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calcInsulin(carbs: Double, bloodSugarLevel: String): String {
        val currentTime = LocalTime.now()
        val eightPM = LocalTime.of(20, 0) // 20:00 hours
        val threeAM = LocalTime.of(3, 0)  // 03:00 hours
        val carbsPerUnit = _carbsPerUnit.value ?: 0.0
        val adjFactor = _adjFactor.value ?: 0.0
        val bsl = bloodSugarLevel.toDoubleOrNull() ?: 0.0
        val correctionDoseLim: Double
        val correctionDose: Double
        if (currentTime.isAfter(eightPM) || currentTime.isBefore(threeAM)) {
            correctionDoseLim = 200.0
        } else {
            correctionDoseLim = 160.0
        }
        if (bsl > correctionDoseLim) {
            correctionDose = ceil(((bsl - correctionDoseLim) / adjFactor))
        } else {
            correctionDose = 0.0
        }
        return if (carbsPerUnit > 0) {
            val dose = round((carbs / carbsPerUnit) + correctionDose)
            "%.2f".format(dose)
        } else {
            "Unavailable"
        }
    }
}