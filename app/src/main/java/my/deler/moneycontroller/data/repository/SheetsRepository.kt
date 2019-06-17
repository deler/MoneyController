package my.deler.moneycontroller.data.repository

import my.deler.moneycontroller.data.models.CategoryData
import my.deler.moneycontroller.data.models.ExpenseData
import my.deler.moneycontroller.data.models.IncomingData

interface SheetsRepository {
    suspend fun getCategories(): List<CategoryData>
    suspend fun addExpense(expense: ExpenseData)
    suspend fun addIncoming(incoming: IncomingData)
}