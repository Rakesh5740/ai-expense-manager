package com.example.aiexpensemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiexpensemanager.data.local.ExpenseEntity
import com.example.aiexpensemanager.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    val expenses = repository
        .getAllExpenses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addExpense(expenseEntity: ExpenseEntity) {
        viewModelScope.launch {
            repository.insertExpense(expenseEntity)
        }
    }

    fun updateExpense(expenseEntity: ExpenseEntity) {
        viewModelScope.launch {
            repository.updateExpense(expenseEntity)
        }
    }

    fun deleteExpense(expenseEntity: ExpenseEntity) {
        viewModelScope.launch {
            repository.deleteExpense(expenseEntity)
        }
    }
}