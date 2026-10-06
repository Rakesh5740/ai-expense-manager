package com.example.aiexpensemanager.data.ai

sealed interface AiExpenseUiState {
    data object Idle: AiExpenseUiState
    data object Loading: AiExpenseUiState

    data class Success(val message: String): AiExpenseUiState
    data class Error(val message: String): AiExpenseUiState
}