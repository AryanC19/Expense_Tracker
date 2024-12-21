package com.example.avengers_tracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.avengers_tracker.R
import com.example.avengers_tracker.data.ExpenseDataBase
import com.example.avengers_tracker.data.dao.ExpenseDao
import com.example.avengers_tracker.data.model.ExpenseEntity
import kotlinx.coroutines.launch

class HomeViewModel(private val dao: ExpenseDao) : ViewModel() {
    // LiveData or StateFlow for observing data
    val expenses = dao.getAllExpenses()

    // Function to delete an expense
    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.deleteExpense(expense) // Call the delete function from dao
        }
    }

    // Function to calculate balance
    fun getBalance(list: List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Expense") {
                total -= it.amount
            } else {
                total += it.amount
            }
        }
        return "$ ${total}"
    }

    // Function to get total income
    fun getTotalIncome(list: List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Income") {
                total += it.amount
            }
        }
        return "$ ${total}"
    }

    // Function to get total expense
    fun getTotalExpense(list: List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Expense") {
                total += it.amount
            }
        }
        return "$ ${total}"
    }

    // Function to get item icon
    fun getItemIcon(item: ExpenseEntity): Int {
        return when (item.category) {
            "GooglePay" -> R.drawable.ic_google
            "Paypal" -> R.drawable.ic_paypal
            "PhonePay" -> R.drawable.ic_phonepay
            "PayTM" -> R.drawable.ic_paytm
            "Visa" -> R.drawable.ic_visa
            else -> R.drawable.ic_cash
        }
    }
}


class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()  // Fetching the DAO here
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T // Passing the DAO to the ViewModel constructor
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

