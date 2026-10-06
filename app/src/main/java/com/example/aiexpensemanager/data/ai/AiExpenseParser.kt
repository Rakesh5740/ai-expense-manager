package com.example.aiexpensemanager.data.ai

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class AiExpenseParser {

    private val gson = Gson()

    fun parse(json: String): AiExpense {
        if (json.isBlank()) {
            throw IllegalArgumentException("AI returned an empty response")
        }

        return try {
            gson.fromJson(json, AiExpense::class.java)
                ?: throw IllegalArgumentException("AI returned an empty expense")
        } catch (e: JsonSyntaxException) {
            throw IllegalArgumentException(
                "AI returned an invalid expense format"
            )
        }
    }
}