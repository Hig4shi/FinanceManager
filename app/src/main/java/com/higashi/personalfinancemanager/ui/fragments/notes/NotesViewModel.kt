package com.higashi.personalfinancemanager.ui.fragments.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.higashi.personalfinancemanager.data.entities.Transaction
import com.higashi.personalfinancemanager.data.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: FinanceRepository) : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaction>>()
    val categories: LiveData<List<Transaction>> = _transactions

    fun deleteTransaction(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTransaction(id)
    }

    fun getTransactionsAndCategories(startDate: Long, endDate: Long) =
        repository.getTransactionsAndCategories(startDate, endDate)

    fun getTotalQuantity(typeOfOperation: String, startDate: Long, endDate: Long) = repository.getTotalQuantity(typeOfOperation, startDate, endDate)

    fun getBalance(startDate: Long, endDate: Long) = repository.getBalance(startDate, endDate)
}