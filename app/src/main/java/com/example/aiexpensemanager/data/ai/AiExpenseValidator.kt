package com.example.aiexpensemanager.data.ai

class AiExpenseValidator {

    fun validate(expense: AiExpense) {
        require(expense.amount > 0) {
            "Expense amount must be greater than zero"
        }

        require(expense.category.isNotBlank()) {
            "Expense category is missing"
        }

        require(expense.description.isNotBlank()) {
            "Expense description is missing"
        }

        require(expense.date.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            "Expense date has invalid format"
        }
    }
}