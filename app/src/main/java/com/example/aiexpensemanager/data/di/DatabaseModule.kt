package com.example.aiexpensemanager.data.di

import android.content.Context
import androidx.room3.Room
import com.example.aiexpensemanager.data.local.ExpenseDao
import com.example.aiexpensemanager.data.local.ExpenseDatabase
import com.example.aiexpensemanager.data.repository.ExpenseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideExpenseDatabase(
        @ApplicationContext context: Context
    ): ExpenseDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            "expense_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(expenseDatabase: ExpenseDatabase): ExpenseDao {
        return expenseDatabase.expenseDao()
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(expenseDao: ExpenseDao): ExpenseRepository {
        return ExpenseRepository(expenseDao)
    }
}