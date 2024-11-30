package com.example.myapplication
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

object ExpensePreferences {

    private const val PREFS_NAME = "expense_prefs"
    private const val KEY_EXPENSES = "expenses_key"

    // Save the Expanses object to SharedPreferences
    fun saveExpenses(context: Context, expenses: Expanses) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        // Convert the list of expenses to JSON
        val json = gson.toJson(expenses.getAllExpensesList()) // Converts list of ExpenseItem to JSON
        editor.putString(KEY_EXPENSES, json)
        editor.apply()
    }

    // Load the Expanses object from SharedPreferences
    fun loadExpenses(context: Context): Expanses {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_EXPENSES, null)

        val expenses = Expanses()

        // If JSON is found, convert it back to a list of ExpenseItem
        if (json != null) {
            val type = object : TypeToken<List<ExpenseItem>>() {}.type
            val expenseList: List<ExpenseItem> = gson.fromJson(json, type)
            expenseList.forEach { expenses.addExpanses(it.tags, it.value, it.date) }
        }

        return expenses
    }
}