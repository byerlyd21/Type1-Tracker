package com.example.cs3200firebasestarter.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.cs3200firebasestarter.ui.navigation.RootNavigation
import com.example.cs3200firebasestarter.ui.theme.CS3200FirebaseStarterTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun App() {
    CS3200FirebaseStarterTheme {
        RootNavigation()
    }
}