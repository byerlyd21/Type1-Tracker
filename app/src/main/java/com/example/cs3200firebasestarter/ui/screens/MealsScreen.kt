package com.example.cs3200firebasestarter.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cs3200firebasestarter.ui.models.Meal
import com.example.cs3200firebasestarter.ui.viewmodels.MealsViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealsScreen(navHostController: NavHostController) {
    val viewModel: MealsViewModel = viewModel()
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val meals by viewModel.meals.observeAsState(initial = listOf())
    var bloodSugarLevel by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = bloodSugarLevel,
                        onValueChange = { bloodSugarLevel = it },
                        label = { Text("Blood Sugar Level") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Spacing between TextField and Button
                }
                Row {
                    TopAppBar(title = { Text("Meals") })
                }
            }
        }
    ) { contentPadding ->
        if (isLoading) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        } else {
            MealList(meals, contentPadding, bloodSugarLevel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MealList(meals: List<Meal>, contentPadding: PaddingValues, bloodSugarLevel: String) {
    LazyColumn(
        modifier = Modifier.padding(contentPadding)  // Apply content padding
    ) {
        items(meals) { meal ->
            MealItem(meal, bloodSugarLevel)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MealItem(meal: Meal, bloodSugarLevel: String) {
    val viewModel: MealsViewModel = viewModel()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = meal.restaurant,
            )
            Text(
                text = "Description: ${meal.description}",
            )
            Text(
                text = "Carbs: ${meal.carbs}",
            )
            Text(
                text = "Insulin Units: ${viewModel.calcInsulin(meal.carbs, bloodSugarLevel)}"
            )
        }
    }
}

