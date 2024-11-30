package com.example.myapplication

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*


@Composable
fun datePicker(): Calendar? {
    // State to hold the selected date as a Calendar instance
    var selectedDate by remember { mutableStateOf<Calendar?>(null) }

    // Current date as Calendar
    val calendar = Calendar.getInstance()

    // Date picker dialog setup
    val context = LocalContext.current
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Update the calendar date
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar // Update the state with the selected Calendar date
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Composable UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the selected date in a human-readable format
        Text(
            text = "Selected Date: ${selectedDate?.time?.toFormattedString() ?: "No date selected"}",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to show the DatePickerDialog
        Button(onClick = { datePickerDialog.show() }) {
            Text(text = "Pick Date")
        }
    }

    // Return the selected Calendar instance (or null if no date is selected)
    return selectedDate
}

fun Date.toFormattedString(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1 // Month is 0-based, so we add 1
    val year = calendar.get(Calendar.YEAR)
    return "$day/$month/$year"
}