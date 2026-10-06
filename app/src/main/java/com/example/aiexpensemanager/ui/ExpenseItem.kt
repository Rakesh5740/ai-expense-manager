package com.example.aiexpensemanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aiexpensemanager.data.local.ExpenseEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExpenseItem(
    expense: ExpenseEntity,
    onDelete:(ExpenseEntity) -> Unit,
    onEdit:(ExpenseEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedDate = SimpleDateFormat(
        "dd MMM yyyy, hh:mm a",
        Locale.getDefault()
    ).format(Date(expense.date))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "${expense.amount}")
            Text(text = expense.category)
            Text(text = expense.merchant)
            Text(text = expense.description)
            Text(text = formattedDate)

            Button(
                onClick = {
                    onEdit(expense)
                }
            ) {
                Text("Edit")
            }

            Button(
                onClick = {
                    onDelete(expense)
                }
            ) {
                Text("Delete")
            }
        }
    }
}