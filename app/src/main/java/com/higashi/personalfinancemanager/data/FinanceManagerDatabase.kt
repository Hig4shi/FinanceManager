package com.higashi.personalfinancemanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.higashi.personalfinancemanager.data.entities.Category
import com.higashi.personalfinancemanager.data.entities.Transaction

@Database(
    entities = [Transaction::class, Category::class],
    version = 1
)
abstract class FinanceManagerDatabase : RoomDatabase() {

    abstract fun getTransactionDao(): TransactionDao

    abstract fun getCategoryDao(): CategoryDao

    companion object {
        @Volatile
        private var instance: FinanceManagerDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FinanceManagerDatabase::class.java,
                "finance_manager_db.db"
            ).build()
    }

}