package com.example.pertemuan6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorApp()
        }
    }
}

@Composable
fun CalculatorApp() {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Kalkulator Sederhana", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = num1,
            onValueChange = { num1 = it },
            label = { Text("Angka 1") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Angka 2") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                result = ((num1.toDoubleOrNull() ?: 0.0) + (num2.toDoubleOrNull() ?: 0.0)).toString()
            }) {
                Text("+")
            }

            Button(onClick = {
                result = ((num1.toDoubleOrNull() ?: 0.0) - (num2.toDoubleOrNull() ?: 0.0)).toString()
            }) {
                Text("-")
            }

            Button(onClick = {
                result = ((num1.toDoubleOrNull() ?: 0.0) * (num2.toDoubleOrNull() ?: 0.0)).toString()
            }) {
                Text("×")
            }

            Button(onClick = {
                val n2 = num2.toDoubleOrNull() ?: 0.0
                result = if (n2 != 0.0) {
                    ((num1.toDoubleOrNull() ?: 0.0) / n2).toString()
                } else {
                    "Tidak bisa bagi 0"
                }
            }) {
                Text("÷")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                num1 = ""
                num2 = ""
                result = "0"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Hasil: $result",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}