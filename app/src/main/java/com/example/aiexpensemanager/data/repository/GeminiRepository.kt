package com.example.aiexpensemanager.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig
import jakarta.inject.Inject

class GeminiRepository @Inject constructor() {

    private val expenseSchema = Schema.obj(
        properties = mapOf(
            "amount" to Schema.double(),
            "category" to Schema.string(),
            "merchant" to Schema.string(),
            "description" to Schema.string(),
            "date" to Schema.string()
        )
    )

    private val expenseModel = Firebase.ai(
        backend = GenerativeBackend.googleAI()
    ).generativeModel(
        modelName = "gemini-3.5-flash-lite",
        generationConfig = generationConfig {
            responseMimeType = "application/json"
            responseSchema = expenseSchema
        }
    )

    suspend fun extractExpense(
        text: String,
        currentDate: String
    ): String {
        val prompt = """
    Extract expense information from this sentence.

    Expense:
    "$text"

    Today's date is:
    $currentDate

    Rules:
    - Extract the expense amount as a positive number.
    - Choose exactly one category from this list:
      Food & Dining
      Groceries
      Transport
      Shopping
      Bills & Utilities
      Entertainment
      Healthcare
      Travel
      Education
      Other
    - If the merchant is mentioned, extract it.
    - If no merchant is mentioned, use "Unknown".
    - Keep the description short and meaningful.
    - If the expense says "today", use today's date.
    - If the expense says "yesterday", use the date one day before today.
    - If a specific date is mentioned, use that date.
    - Never guess the current date.
    - Date must be in yyyy-MM-dd format.

    Return the expense using the provided JSON schema.
""".trimIndent()

        val response = expenseModel.generateContent(prompt)
        return response.text ?: ""
    }
}