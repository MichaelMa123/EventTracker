package com.example.myapplication

import java.text.SimpleDateFormat
import java.util.*

class FuturePlan(private val Exp: Expanses){
    private val futurePlans: MutableList<Triple<Date,String,Int>> = mutableListOf()
    fun addRecurringExpense(name: String, value: Int, date: Date, type: String):Triple<Date,String,Int> {


        Exp.addExpanses(name, value, date)// the first expense
        val nextDate = when (type) {
            "OneTime" -> {// Directly add the expense for one-time
                date
            }
            "Weekly" -> {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.add(Calendar.WEEK_OF_YEAR, 1) // Add one week to the current date
                calendar.time
            }
            "Monthly" -> {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.add(Calendar.MONTH, 1) // Add one month to the current date
                calendar.time
            }
            "Quarterly" -> {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.add(Calendar.MONTH, 3) // Add three months (quarter)
                calendar.time
            }
            else -> date
        }
        Exp.addExpanses(name, value, date)

        // Store the future plan as a Triple<Date, String, Int>
        futurePlans.add(Triple(date, type, value))

        return Triple(nextDate, type, value) // Return the next execution date and type
    }


    fun processRecurringExpenses() {
        val currentDate = Date()
        val iterator = futurePlans.iterator()

        while (iterator.hasNext()) {
            val (nextDate, type,cost) = iterator.next()

            if (!nextDate.after(currentDate)) {
                // Execute the expense logic based on the recurrence type
                when (type) {
                    "Weekly" -> {
                        println("Processing weekly expense.")
                        Exp.addExpanses("repeated Weekly", cost, nextDate)
                        // You can trigger your addExpanses logic here based on your requirements
                    }
                    "Monthly" -> {
                        println("Processing monthly expense.")
                        Exp.addExpanses("repeated Monthly", cost, nextDate)
                        // Logic for monthly recurrence
                    }
                    "Quarterly" -> {
                        println("Processing quarterly expense.")
                        Exp.addExpanses("repeated Quarterly", cost, nextDate)
                        // Logic for quarterly recurrence
                    }
                    else -> {
                        println("One-time or unknown expense type.")
                    }
                }

                // Remove the one-time event after execution, or update the next execution date for recurring ones
                if (type == "OneTime") {
                    iterator.remove() // One-time events can be removed after execution
                } else {
                    // Update the next execution date for recurring expenses
                    val updatedNextDate = when (type) {
                        "Weekly" -> Calendar.getInstance().apply { time = nextDate; add(Calendar.WEEK_OF_YEAR, 1) }.time
                        "Monthly" -> Calendar.getInstance().apply { time = nextDate; add(Calendar.MONTH, 1) }.time
                        "Quarterly" -> Calendar.getInstance().apply { time = nextDate; add(Calendar.MONTH, 3) }.time
                        else -> nextDate
                    }
                    // Replace the old date with the new one
                    iterator.remove()
                    futurePlans.add(Triple(updatedNextDate, type,cost))
                }
            }
        }
    }
}