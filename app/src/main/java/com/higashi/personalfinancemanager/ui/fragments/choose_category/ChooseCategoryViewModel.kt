package com.higashi.personalfinancemanager.ui.fragments.choose_category

import androidx.lifecycle.ViewModel
import com.higashi.personalfinancemanager.data.repository.FinanceRepository

class ChooseCategoryViewModel(private val repository: FinanceRepository) : ViewModel() {

    fun getAllCategories() = repository.getAllCategories()

}