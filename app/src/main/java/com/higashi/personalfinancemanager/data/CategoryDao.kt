package com.higashi.personalfinancemanager.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.higashi.personalfinancemanager.data.entities.Category
import com.higashi.personalfinancemanager.data.entities.Transaction

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category): Long

    @Update
    suspend fun updateCategory(category: Category)

    @Query("SELECT * FROM categories ORDER BY type_of_operation DESC, name ASC")
    fun getAllCategories(): LiveData<List<Category>>

    @Delete
    suspend fun deleteCategory(category: Category)

}