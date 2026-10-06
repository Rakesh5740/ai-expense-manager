package com.example.aiexpensemanager.data.ai

data class AiExpense(
    val amount: Double,
    val category: String,
    val merchant: String,
    val description: String,
    val date: String
)