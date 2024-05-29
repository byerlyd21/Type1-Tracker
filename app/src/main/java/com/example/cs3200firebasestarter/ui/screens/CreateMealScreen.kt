package com.example.cs3200firebasestarter.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cs3200firebasestarter.ui.navigation.Routes
import com.example.cs3200firebasestarter.ui.viewmodels.MealsViewModel

@Composable
fun CreateMealScreen(navHostController: NavHostController) {
    //val navController = rememberNavController()
    var restaurant by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    val viewModel: MealsViewModel = viewModel()
    val mealCreationStatus by viewModel.mealCreationStatus.observeAsState()

    LaunchedEffect(mealCreationStatus) {
        if (mealCreationStatus == true) {
            navHostController.navigate(Routes.meals.route)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create New Meal",
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = restaurant,
            onValueChange = { restaurant = it },
            label = { Text("Restaurant") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = carbs,
            onValueChange = { carbs = it },
            label = { Text("Carbs") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val carbsValue = carbs.toDoubleOrNull() ?: 0.0
                viewModel.createMeal(restaurant, description, carbsValue)
                //navController.navigate(Routes.meals.route)
            }
        ) {
            Text("Submit")
        }
    }
}
