package com.example.cs3200firebasestarter.ui.navigation

data class Screen(val route: String)

object Routes {
    val launchNavigation = Screen("launchnavigation")
    val appNavigation = Screen("appnavigation")
    val launch = Screen("launch")
    val signIn = Screen("signin")
    val signUp = Screen("signup")
    val splashScreen = Screen("splashscreen")
    val home = Screen(route = "home")
    val profile = Screen(route = "profile")
    val meals = Screen(route = "meals")
    val map = Screen(route = "map")
    val learn = Screen(route = "learn")
    val doseCalc = Screen(route = "doseCalc")
    val posts = Screen(route = "posts")
    val createMeal = Screen(route = "createMeal")

}