package com.example.aiexpensemanager.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.aiexpensemanager.ExpenseViewModel
import com.example.aiexpensemanager.data.ai.AiExpenseUiState
import com.example.aiexpensemanager.data.local.ExpenseEntity

@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel,
    modifier: Modifier = Modifier
) {
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()
    val aiState by viewModel.aiState.collectAsStateWithLifecycle()

    var expenseBeingEdited by remember { mutableStateOf<ExpenseEntity?>(null) }
    var aiExpenseText by remember { mutableStateOf("") }

    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var merchant by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AI Expense Manager",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }

            item {
                Text(
                    text = "Add Expense with AI",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                OutlinedTextField(
                    value = aiExpenseText,
                    onValueChange = { aiExpenseText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Describe your expense")
                    },
                    placeholder = {
                        Text("e.g. I spent ₹450 on dinner yesterday")
                    },
                    singleLine = false
                )
            }

            item {
                Button(
                    onClick = {
                        viewModel.addExpenseWithAi(aiExpenseText)
                        aiExpenseText = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = aiExpenseText.isNotBlank() &&
                            aiState !is AiExpenseUiState.Loading
                ) {
                    Text("Add with AI")
                }
            }

            item {
                when (val state = aiState) {

                    AiExpenseUiState.Idle -> {
                        // Nothing to show
                    }

                    AiExpenseUiState.Loading -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI processing...",
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }

                    is AiExpenseUiState.Success -> {
                        Text(
                            text = state.message,
                            modifier = modifier.padding(top = 8.dp)
                        )
                    }

                    is AiExpenseUiState.Error -> {
                        Text(
                            text = "Error: ${state.message}",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") }
                )
            }
            item {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") }
                )
            }
            item {
                OutlinedTextField(
                    value = merchant,
                    onValueChange = { merchant = it },
                    label = { Text("Merchant") }
                )
            }
            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
            }
            item {
                Button(
                    onClick = {
                        val amountValue = amount.toDoubleOrNull()

                        if (amountValue != null) {
                            viewModel.addExpense(
                                amount = amountValue,
                                category = category,
                                merchant = merchant,
                                description = description,
                                date = System.currentTimeMillis()
                            )
                            amount = ""
                            category = ""
                            merchant = ""
                            description = ""
                        }
                    }
                ) {
                    Text("Save Expense")
                }
            }

            item {
                Text(
                    text = "Total expenses: ${expenses.size}"
                )
            }

            items(expenses) { expense ->
                ExpenseItem(
                    expense = expense,
                    onDelete = { expenseToDelete ->
                        viewModel.deleteExpense(expenseEntity = expenseToDelete)
                    },
                    onEdit = { expenseToEdit ->
                        expenseBeingEdited = expenseToEdit
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.padding(bottom = 16.dp))
            }
        }

        expenseBeingEdited?.let { expense ->

            var editAmount by remember(expense.id) {
                mutableStateOf(expense.amount.toString())
            }
            var editCategory by remember(expense.id) {
                mutableStateOf(expense.category)
            }
            var editMerchant by remember(expense.id) {
                mutableStateOf(expense.merchant)
            }
            var editDescription by remember(expense.id) {
                mutableStateOf(expense.description)
            }

            AlertDialog(
                onDismissRequest = {
                    expenseBeingEdited = null
                },
                title = {
                    Text(text = "Edit Expense")
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                        OutlinedTextField(
                            value = editAmount,
                            onValueChange = { editAmount = it },
                            label = { Text("Amount") }
                        )

                        OutlinedTextField(
                            value = editCategory,
                            onValueChange = { editCategory = it },
                            label = { Text("Category") }
                        )

                        OutlinedTextField(
                            value = editMerchant,
                            onValueChange = { editMerchant = it },
                            label = { Text("Merchant") }
                        )

                        OutlinedTextField(
                            value = editDescription,
                            onValueChange = { editDescription = it },
                            label = { Text("Description") }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val amount = editAmount.toDoubleOrNull()

                            if (amount != null) {
                                viewModel.updateExpense(
                                    expense.copy(
                                        amount = amount,
                                        category = editCategory,
                                        merchant = editMerchant,
                                        description = editDescription
                                    )
                                )
                                expenseBeingEdited = null
                            }
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            expenseBeingEdited = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}