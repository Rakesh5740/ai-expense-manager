package com.example.aiexpensemanager.data.repository

import com.example.aiexpensemanager.data.local.ExpenseDao
import com.example.aiexpensemanager.data.local.ExpenseEntity
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao
) {
    suspend fun insertExpense(expense: ExpenseEntity) {
        expenseDao.insertExpense(expense)
    }

    suspend fun updateExpense(expense: ExpenseEntity) {
        expenseDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: ExpenseEntity) {
        expenseDao.deleteExpense(expense)
    }

    fun getAllExpenses(): Flow<List<ExpenseEntity>> {
        return expenseDao.getExpenses()
    }
}