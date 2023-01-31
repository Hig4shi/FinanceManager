package com.higashi.personalfinancemanager.data

import android.os.FileObserver.DELETE
import androidx.lifecycle.LiveData
import androidx.room.*
import com.higashi.personalfinancemanager.data.entities.DataForDiagram
import com.higashi.personalfinancemanager.data.entities.Transaction
import com.higashi.personalfinancemanager.data.entities.TransactionAndCategory

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransaction(id: Long)

    @Query("SELECT SUM(transactions.amount) FROM transactions " +
            "INNER JOIN categories ON transactions.category_id = categories.id " +
            "WHERE categories.type_of_operation = :typeOfOperation AND" +
            "(transactions.date >= :startDate AND transactions.date <= :endDate)")
    fun getTotalQuantity(typeOfOperation: String, startDate: Long, endDate: Long): LiveData<Long>

    @Query("SELECT (SELECT SUM(transactions.amount) FROM transactions " +
            "INNER JOIN categories ON transactions.category_id = categories.id " +
            "WHERE categories.type_of_operation = 'Revenue' AND" +
            "(transactions.date >= :startDate AND transactions.date <= :endDate)) - " +
            "(SELECT SUM(transactions.amount) FROM transactions INNER JOIN categories " +
            "ON transactions.category_id = categories.id " +
            "WHERE categories.type_of_operation = 'Expense' AND" +
            "(transactions.date >= :startDate AND transactions.date <= :endDate))")
    fun getBalance(startDate: Long, endDate: Long): LiveData<Long>

    @Query("SELECT categories.id as category_id, categories.name, categories.type_of_operation, transactions.id, " +
            "transactions.description, transactions.amount, transactions.date " +
            "FROM transactions INNER JOIN categories ON transactions.category_id = categories.id " +
            "WHERE transactions.date >= :startDate AND transactions.date <= :endDate " +
            "ORDER BY transactions.id DESC")
    fun getTransactionsAndCategories(startDate: Long, endDate: Long): LiveData<List<TransactionAndCategory>>

    @Query("SELECT categories.name, SUM(transactions.amount) as amount FROM transactions " +
            "INNER JOIN categories ON transactions.category_id = categories.id " +
            "WHERE categories.type_of_operation = :typeOfOperation AND " +
            "(transactions.date >= :startDate AND transactions.date <= :endDate) GROUP BY categories.name")
    fun getDataForDiagram(typeOfOperation: String, startDate: Long, endDate: Long): LiveData<List<DataForDiagram>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionById(id: Long): LiveData<Transaction>

    @Query("SELECT categories.id as category_id, categories.name, categories.type_of_operation, transactions.id, " +
            "transactions.description, transactions.amount, transactions.date " +
            "FROM transactions INNER JOIN categories ON transactions.category_id = categories.id " +
            "WHERE transactions.id = :id")
    fun getTransactionAndCategory(id: Long): LiveData<TransactionAndCategory>
}