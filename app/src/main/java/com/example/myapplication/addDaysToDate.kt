package com.example.myapplication
import java.util.Calendar
import java.util.Date

fun addDaysToDate(date: Date, days: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.DAY_OF_YEAR, days)  // Adds specified days
    return calendar.time
}
