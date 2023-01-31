package com.higashi.personalfinancemanager.ui.fragments.all_categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.higashi.personalfinancemanager.data.entities.Category
import com.higashi.personalfinancemanager.data.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllCategoriesViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    fun getAllCategories() = repository.getAllCategories()

    fun insertCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCategory(category)
    }

    fun deleteCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCategory(category)
    }

}