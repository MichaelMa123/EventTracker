package com.example.myapplication

import java.util.*

data class EventList(val tags: String, val date: Date, val description: String)

class Events {
    private val listOfEvents: MutableList<EventList> = mutableListOf()

    fun createEvent(tag: String, date: Date, description: String?) {
        // Safely handle null description
        val safeDescription = description ?: "No description provided"
        listOfEvents.add(EventList(tag, date, safeDescription))
    }

    fun remove(tag: String, date: Date, description: String) {
        listOfEvents.removeAll { it.tags == tag && it.date == date && it.description == description }
    }

    fun searchByTag(tag: String): List<EventList> {
        return listOfEvents.filter { it.tags.contains(tag, ignoreCase = true) }
    }

    fun listSearchedEvent(day: Int, month: Int, year: Int): List<EventList> {
        val calendar = Calendar.getInstance()
        return listOfEvents.filter { event ->
            calendar.time = event.date
            val eventDay = calendar.get(Calendar.DAY_OF_MONTH)
            val eventMonth = calendar.get(Calendar.MONTH) + 1 // Month is 0-based
            val eventYear = calendar.get(Calendar.YEAR)
            eventDay == day && eventMonth == month && eventYear == year
        }
    }

    fun listNearEvents(isUpcoming: Boolean, x: Int): List<EventList> {
    val currentDate = Date()

    val filteredEvents = if (isUpcoming) {
        listOfEvents.filter { it.date.after(currentDate) }
            .sortedBy { it.date.time }  // Sort by ascending date for upcoming events
    } else {
        listOfEvents.filter { it.date.before(currentDate) }
            .sortedByDescending { it.date.time }  // Sort by descending date for past events
    }

    // Return the top 'x' events if x > 0, else return all
    return if (x > 0) filteredEvents.take(x) else filteredEvents
}


    fun returnAllEvents(): List<EventList> {
        return listOfEvents
    }
}
