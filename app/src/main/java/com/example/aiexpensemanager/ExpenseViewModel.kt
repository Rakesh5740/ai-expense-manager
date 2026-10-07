package com.example.aiexpensemanager

import android.util.Log
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiexpensemanager.data.ai.AiExpenseMapper
import com.example.aiexpensemanager.data.ai.AiExpenseParser
import com.example.aiexpensemanager.data.ai.AiExpenseUiState
import com.example.aiexpensemanager.data.ai.AiExpenseValidator
import com.example.aiexpensemanager.data.local.ExpenseEntity
import com.example.aiexpensemanager.data.repository.ExpenseRepository
import com.example.aiexpensemanager.data.repository.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    private val geminiRepository: GeminiRepository
) : ViewModel() {

    private val aiExpenseParser = AiExpenseParser()
    private val aiExpenseMapper = AiExpenseMapper()

    private val _aiState = MutableStateFlow<AiExpenseUiState>(AiExpenseUiState.Idle)
    val aiState = _aiState.asStateFlow()

    private val aiExpenseValidator = AiExpenseValidator()

    val expenses = repository
        .getAllExpenses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addExpenseWithAi(text: String) {
        viewModelScope.launch {
            try {
                _aiState.value = AiExpenseUiState.Loading

                val currentDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.US
                ).format(Date())

                val json = geminiRepository.extractExpense(
                    text = text,
                    currentDate = currentDate
                )
                Log.d("AI_EXPENSE", "JSON from Gemini: $json")

                val aiExpense = aiExpenseParser.parse(json)
                aiExpenseValidator.validate(aiExpense)
                val expenseEntity = aiExpenseMapper.toExpenseEntity(aiExpense)
                repository.insertExpense(expenseEntity)

                _aiState.value = AiExpenseUiState.Success(
                    message = "Expense added successfully"
                )

            } catch (e: Exception) {
                Log.e("AI_EXPENSE", "Error adding expense with AI", e)
                _aiState.value = AiExpenseUiState.Error(
                    "Failed to add expense: ${e.message}"
                )
            }
        }
    }

    fun addExpense(
        amount: Double,
        category: String,
        merchant: String,
        description: String,
        date: Long
    ) {
        val expense = ExpenseEntity(
            amount = amount,
            category = category,
            merchant = merchant,
            description = description,
            date = date
        )
        viewModelScope.launch {
            repository.insertExpense(expense)
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