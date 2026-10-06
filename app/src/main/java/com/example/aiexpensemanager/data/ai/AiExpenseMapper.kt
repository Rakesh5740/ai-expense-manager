package com.example.aiexpensemanager.data.ai

import com.example.aiexpensemanager.data.local.ExpenseEntity
import java.text.SimpleDateFormat
import java.util.Locale

class AiExpenseMapper {

    fun toExpenseEntity(aiExpense: AiExpense): ExpenseEntity {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        val date = dateFormat.parse(aiExpense.date)?.time
            ?: System.currentTimeMillis()

        return ExpenseEntity(
            amount = aiExpense.amount,
            category = aiExpense.category,
            merchant = aiExpense.merchant,
            description = aiExpense.description,
            date = date
        )
    }
}