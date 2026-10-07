package com.example.aiexpensemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aiexpensemanager.ui.ExpenseScreen
import com.example.aiexpensemanager.ui.theme.AIExpenseManagerTheme
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.initialize
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Firebase.initialize(context = this)

        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )

        setContent {
            AIExpenseManagerTheme {
                val expenseViewModel: ExpenseViewModel = hiltViewModel()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ExpenseScreen(
                        viewModel = expenseViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}