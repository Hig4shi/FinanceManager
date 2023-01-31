package com.higashi.personalfinancemanager.ui.fragments.details_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.higashi.personalfinancemanager.data.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsNoteViewModel(private val repository: FinanceRepository) : ViewModel() {

    fun deleteTransaction(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTransaction(id)
    }

    fun getTransactionById(id: Long) = repository.getTransactionById(id)

    fun getTransactionAndCategory(id: Long) = repository.getTransactionAndCategory(id)

}