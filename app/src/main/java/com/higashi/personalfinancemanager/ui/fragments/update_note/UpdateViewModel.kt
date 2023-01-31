package com.higashi.personalfinancemanager.ui.fragments.update_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.higashi.personalfinancemanager.data.entities.Transaction
import com.higashi.personalfinancemanager.data.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateViewModel(private val repository: FinanceRepository) : ViewModel() {

    fun updateTransaction(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTransaction(transaction)
    }

}