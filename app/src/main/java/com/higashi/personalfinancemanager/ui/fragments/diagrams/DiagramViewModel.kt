package com.higashi.personalfinancemanager.ui.fragments.diagrams

import androidx.lifecycle.ViewModel
import com.higashi.personalfinancemanager.data.repository.FinanceRepository

class DiagramViewModel(private val repository: FinanceRepository) : ViewModel() {

    fun getDataForDiagram(typeOfOperation: String, startDate: Long, endDate: Long) = repository.getDataForDiagram(typeOfOperation, startDate, endDate)

}