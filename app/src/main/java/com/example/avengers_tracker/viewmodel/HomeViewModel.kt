package com.example.avengers_tracker.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.avengers_tracker.R
import com.example.avengers_tracker.data.ExpenseDataBase
import com.example.avengers_tracker.data.dao.ExpenseDao
import com.example.avengers_tracker.data.model.ExpenseEntity

class HomeViewModel(dao: ExpenseDao) : ViewModel() {
    val expenses = dao.getAllExpenses()

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

    fun getTotalIncome(list: List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Income") {
                total += it.amount
            }
        }
        return "$ ${total}"

    }

    fun getTotalExpense(list: List<ExpenseEntity>): String {
        var total = 0.0
        list.forEach {
            if (it.type == "Expense") {
                total += it.amount
            } else {
                Log.d("ExpenseDebug", "Skipping item: ${it.title}, type: ${it.type}")
            }
        }
        return "$ ${total}"
    }


    fun getItemIcon(item: ExpenseEntity): Int {
        return if (item.category == "GooglePay") {
            R.drawable.ic_google
        } else if (item.category == "Paypal") {
            R.drawable.ic_paypal
        } else if (item.category == "PhonePay") {
            R.drawable.ic_phonepay
        } else if (item.category == "PayTM") {
            R.drawable.ic_paytm
        } else if (item.category == "Visa") {
            R.drawable.ic_visa
        } else {
            R.drawable.ic_cash
        }

    }
}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val dao = ExpenseDataBase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}