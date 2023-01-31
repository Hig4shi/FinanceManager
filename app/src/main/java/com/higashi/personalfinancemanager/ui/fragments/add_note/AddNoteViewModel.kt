package com.higashi.personalfinancemanager.ui.fragments.add_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.higashi.personalfinancemanager.data.entities.Transaction
import com.higashi.personalfinancemanager.data.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNoteViewModel(private val repository: FinanceRepository) : ViewModel() {

    fun insertTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTransaction(transaction)
    }

}