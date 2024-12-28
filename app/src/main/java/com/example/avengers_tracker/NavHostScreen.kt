package com.example.avengers_tracker.android

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.avengers_tracker.AddExpense
import com.example.avengers_tracker.HomeScreen
import com.example.avengers_tracker.LoginScreen
import com.example.avengers_tracker.data.model.ExpenseEntity
import com.google.gson.Gson

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

        composable(
            route = "/add?expenseEntity={expenseEntity}",
            arguments = listOf(navArgument("expenseEntity") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val expenseEntityJson = backStackEntry.arguments?.getString("expenseEntity")
            val expenseEntity = expenseEntityJson?.let { json ->
                // Deserialize JSON to ExpenseEntity
                Gson().fromJson(
                    json,
                    ExpenseEntity::class.java
                ) // Deserialize JSON to ExpenseEntity

            }
            AddExpense(navController, expenseEntity)
        }


    }
}