package com.example.avengers_tracker.android

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.avengers_tracker.AddExpense
import com.example.avengers_tracker.HomeScreen
import com.example.avengers_tracker.LoginScreen

@Composable
fun NavHostScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "/login") {
        composable(route = "/login") {
            LoginScreen(navController) // Pass the navController here
        }
        composable(route = "/home") {
            HomeScreen(navController)
        }
        composable(route = "/add") {
            AddExpense(navController)
        }
    }
}