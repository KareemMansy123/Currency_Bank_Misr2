package com.example.currency_bank_misr.presintation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.currency_bank_misr.ui.theme.Currency_Bank_MisrTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Currency_Bank_MisrTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        TravelSpinnerExample()
                    }

                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TravelSpinnerExample() {
        // Dummy data for the spinners
        val fromOptions = listOf("USD", "EGP", "EUR", "GBP")
        val toOptions = listOf("USD", "EGP", "EUR", "GBP")
        val amount = remember { mutableStateOf(0) }
        val result = remember { mutableStateOf(0) }
        // State for selected options
        var fromSelected by remember { mutableStateOf(fromOptions.first()) }
        var toSelected by remember { mutableStateOf(toOptions.first()) }
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                Arrangement.SpaceBetween
            ) {
                // FROM Spinner
                Row(modifier = Modifier.weight(1f)) {
                    DynamicSpinner(
                        label = "From",
                        options = fromOptions,
                        selectedOption = fromSelected,
                        onOptionSelected = { fromSelected = it },
                    )
                }

                Spacer(modifier = Modifier.height(10.dp)) // Space between spinners

                // button to convert the amount from the selected options
                Button(
                    onClick = {
                        result.value = amount.value
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Convert")
                }
                // TO Spinner
                Row(modifier = Modifier.weight(1f)) {
                    DynamicSpinner(
                        label = "To",
                        options = toOptions,
                        selectedOption = toSelected,
                        onOptionSelected = { toSelected = it }
                    )
                }
            }
            // create 2 text fields for the amount and the result of the conversion from the selected options

            Row(
                modifier = Modifier.padding(16.dp),
                Arrangement.SpaceBetween
            ) {
                var text by remember { mutableStateOf("") }
                TextField(
                    value = text,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } || newValue.isBlank()) {
                            text = newValue
                            amount.value = newValue.toIntOrNull() ?: 0
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("Amount") },
                    placeholder = { Text("Enter amount") },
                    modifier = Modifier.width(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp)) // Space before the details button
                Text(
                    text = "Result: ${result.value}",
                    modifier = Modifier.width(200.dp)
                )

            }

            Spacer(modifier = Modifier.height(16.dp)) // Space before the details button

            Button(onClick = { /* Handle details action */ }) {
                Text("Details")
            }
        }


    }

    @Composable
    fun DynamicSpinner(
        label: String,
        options: List<String>,
        selectedOption: String,
        onOptionSelected: (String) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = selectedOption,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .padding(16.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }

}