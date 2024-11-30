package com.example.myapplication
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object EventPreferences {

    private const val PREFS_NAME = "expense_prefs"
    private const val KEY_EVENT = "expenses_key"

    // Save the Expanses object to SharedPreferences
    fun saveExpenses(context: Context, events: Events) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        // Convert the list of expenses to JSON
        val json = gson.toJson(events.returnAllEvents()) // Converts list of ExpenseItem to JSON
        editor.putString(KEY_EVENT, json)
        editor.apply()
    }

    // Load the Expanses object from SharedPreferences
    fun loadExpenses(context: Context): Events {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_EVENT, null)

        val events = Events()

        // If JSON is found, convert it back to a list of ExpenseItem
        if (json != null) {
            val type = object : TypeToken<List<EventList>>() {}.type
            val ListOfEvent: List<EventList> = gson.fromJson(json, type)
            ListOfEvent.forEach { events.createEvent(it.tags, it.date,it.description) }
        }

        return events
    }
}