package com.higashi.personalfinancemanager.data.repository

import com.higashi.personalfinancemanager.data.FinanceManagerDatabase
import com.higashi.personalfinancemanager.data.entities.Category
import com.higashi.personalfinancemanager.data.entities.Transaction

class FinanceRepository(
    private val database: FinanceManagerDatabase
){
    suspend fun insertTransaction(transaction: Transaction) =
        database.getTransactionDao().insertTransaction(transaction)

    suspend fun updateTransaction(transaction: Transaction) =
        database.getTransactionDao().updateTransaction(transaction)

    fun getAllTransactions() = database.getTransactionDao().getAllTransactions()

    suspend fun deleteTransaction(id: Long) =
        database.getTransactionDao().deleteTransaction(id)

    suspend fun insertCategory(category: Category) =
        database.getCategoryDao().insertCategory(category)

    suspend fun updateCategory(category: Category) =
        database.getCategoryDao().updateCategory(category)

    fun getAllCategories() = database.getCategoryDao().getAllCategories()

    suspend fun deleteCategory(category: Category) =
        database.getCategoryDao().deleteCategory(category)

    fun getTransactionsAndCategories(startDate: Long, endDate: Long) =
        database.getTransactionDao().getTransactionsAndCategories(startDate, endDate)

    fun getTotalQuantity(typeOfOperation: String, startDate: Long, endDate: Long) =
        database.getTransactionDao().getTotalQuantity(typeOfOperation, startDate, endDate)

    fun getBalance(startDate: Long, endDate: Long) = database.getTransactionDao().getBalance(startDate, endDate)

    fun getDataForDiagram(typeOfOperation: String, startDate: Long, endDate: Long) =
        database.getTransactionDao().getDataForDiagram(typeOfOperation, startDate, endDate)

    fun getTransactionById(id: Long) = database.getTransactionDao().getTransactionById(id)

    fun getTransactionAndCategory(id: Long) = database.getTransactionDao().getTransactionAndCategory(id)
}