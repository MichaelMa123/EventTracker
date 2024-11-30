package com.example.myapplication

import androidx.compose.material3.SelectableDates
import java.util.Date

// Data class to represent an expense item
data class ExpenseItem(val tags: String, val value: Int, val date: Date)

open class Expanses {

    private val moneyList: MutableList<ExpenseItem> = mutableListOf()

    // Method to add an expense to the list
    fun addExpanses(name: String, value: Int, date: Date) {
        moneyList.add(ExpenseItem(name, value, date))
    }

    // Method to calculate the total cost
    open fun getCost(startDate: Date? = null, endDate: Date? = null): Int {
    return moneyList
        .filter { expense ->
            (startDate == null || expense.date >= startDate) &&
            (endDate == null || expense.date <= endDate)
        }
        .sumOf { it.value }
    }
    open fun getExpensesInRange(startDate: Date? = null, endDate: Date? = null): List<ExpenseItem> {
    return moneyList.filter { expense ->
        (startDate == null || expense.date >= startDate) &&
        (endDate == null || expense.date <= endDate)
    }
    }

    fun allExpanses(): String{
        var lists=""
        for (expense in moneyList){
            lists+="${expense.tags} ${expense.value} ${expense.date} \n"
        }
        return lists
    }

    fun getSpend(tags:String):String{
    var lists=""
        for (expense in moneyList){
                if (expense.tags==tags) {
                    lists+="${expense.tags} ${expense.value} ${expense.date} \n"
                }
            }
        return lists
    }

    fun getAllExpensesList():List<ExpenseItem>{
        return moneyList
    }

    fun removeExpanses(tags:String){
        moneyList.removeAll { it.tags == tags }
    }
    fun searchExpensesByTag( tag: String): List<ExpenseItem> {
        // Filter the list by the tag
        val filteredByTag = moneyList.filter { it.tags == tag }

        // If multiple items have the same tag, sort by date
        return filteredByTag.sortedBy { it.date }
    }

}