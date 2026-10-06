package com.example.aiexpensemanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.aiexpensemanager.ExpenseViewModel
import com.example.aiexpensemanager.data.local.ExpenseEntity

@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel,
    modifier: Modifier = Modifier
) {
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()

    var expenseBeingEdited by remember { mutableStateOf<ExpenseEntity?>(null) }

    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var merchant by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "AI Expense Manager"
        )

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") }
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") }
        )

        OutlinedTextField(
            value = merchant,
            onValueChange = { merchant = it },
            label = { Text("Merchant") }
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )

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

        Text(
            text = "Total expenses: ${expenses.size}"
        )

        LazyColumn {
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