package com.example.aiexpensemanager.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey


@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val description: String,
    val date: Long,
    val category: String,
    val merchant: String
)