package com.example.cs3200firebasestarter.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs3200firebasestarter.ui.viewmodels.ProfileViewModel
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.compose.runtime.livedata.observeAsState
import com.example.cs3200firebasestarter.ui.models.Profile


@Composable
fun ProfileScreen(navHostController: NavHostController) {
    var inEditMode by remember { mutableStateOf(false) }
    val viewModel: ProfileViewModel = viewModel()
    val profile = viewModel.profile.value
    val isLoading = viewModel.isLoading.observeAsState(initial = true)

    var name by remember { mutableStateOf(TextFieldValue(viewModel.profile.value?.name ?: "")) }
    var carbsPerUnit by remember { mutableStateOf(TextFieldValue(viewModel.profile.value?.carbsPerUnit?.toString() ?: "")) }
    var adjustmentFactor by remember { mutableStateOf(TextFieldValue(viewModel.profile.value?.adjustmentFactor?.toString() ?: "")) }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadProfileData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        if (isLoading.value) {
            CircularProgressIndicator()
        } else {
            Text(
                text = "Personal Statistics",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            if (inEditMode) {
                // Editable fields
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") })
                OutlinedTextField(
                    value = carbsPerUnit,
                    onValueChange = { carbsPerUnit = it },
                    label = { Text("Carbs Per Unit") })
                OutlinedTextField(
                    value = adjustmentFactor,
                    onValueChange = { adjustmentFactor = it },
                    label = { Text("Adjustment Factor") })

                Button(onClick = {
                    inEditMode = false
                    viewModel.updateProfileData(
                        updatedProfile = Profile(
                            userId = profile?.userId,
                            name = name.text,
                            carbsPerUnit = carbsPerUnit.text.toDoubleOrNull() ?: 0.0,
                            adjustmentFactor = adjustmentFactor.text.toDoubleOrNull() ?: 0.0
                        )
                    )
                    Log.d("ProfileScreen", "updateProfileData called with: updatedProfile")
                }) {
                    Text("Save")
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = "Name: ${profile?.name ?: "N/A"}",)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Carbs Per Unit of Insulin: ${profile?.carbsPerUnit ?: "N/A"}", )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Adjustment Factor: ${profile?.adjustmentFactor ?: "N/A"}", )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { inEditMode = true }) {
                            Text("Edit")
                        }
                    }
                }
            }
        }
    }
}


