package com.example.avengers_tracker.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.avengers_tracker.R
import com.example.avengers_tracker.data.ExpenseDataBase
import com.example.avengers_tracker.data.dao.ExpenseDao
import com.example.avengers_tracker.data.model.ExpenseEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf


class HomeViewModel(private val dao: ExpenseDao) : ViewModel() {

    private val _expenses = MutableStateFlow<List<ExpenseEntity>>(emptyList())
    val expenses: StateFlow<List<ExpenseEntity>> = _expenses

    private val _filteredExpenses = MutableStateFlow<List<ExpenseEntity>>(emptyList())
    val filteredExpenses: StateFlow<List<ExpenseEntity>> = _filteredExpenses

    init {
        // Collect expenses from DAO and update _expenses
        viewModelScope.launch {
            dao.getAllExpenses().collect { expenseList ->
                _expenses.value = expenseList
            }
        }
    }


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private fun filterTransactions(query: String) {
        val expenseList = _expenses.value
        if (query.isEmpty()) {
            _filteredExpenses.value = expenseList
        } else {
            val filtered = expenseList.filter { expense ->
                expense.title.lowercase().contains(query.lowercase()) ||
                        expense.type.lowercase().contains(query.lowercase())
            }
            _filteredExpenses.value = filtered
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterTransactions(query)
    }


    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.deleteExpense(expense)
            // Update filtered list after deletion
            val currentQuery = _searchQuery.value
            if (currentQuery.isNotEmpty()) {
                filterTransactions(currentQuery)
            }
        }
    }


    fun getBalance(list: List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Expense") {
                total -= it.amount
            } else {
                total += it.amount
            }
        }
        return "$ $total"
    }

    fun getTotalIncome(list: List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Income") {
                total += it.amount
            }
        }
        return "$ $total"
    }

    fun getTotalExpense(list: List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Expense") {
                total += it.amount
            }
        }
        return "$ $total"
    }

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

