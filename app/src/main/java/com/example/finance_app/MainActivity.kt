package com.example.finance_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.finance_app.ui.FinanceApp
import com.example.finance_app.ui.Tokens
import com.example.finance_app.ui.theme.FinanceappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinanceappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Tokens.Bg,
                ) {
                    FinanceApp()
                }
            }
        }
    }
}
