package com.higashi.personalfinancemanager.ui.fragments.add_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.higashi.personalfinancemanager.data.entities.Category
import com.higashi.personalfinancemanager.data.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddCategoryViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    fun insertCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCategory(category)
    }

    fun updateCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateCategory(category)
    }

}