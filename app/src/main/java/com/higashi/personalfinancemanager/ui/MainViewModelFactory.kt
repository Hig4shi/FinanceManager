package com.higashi.personalfinancemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.higashi.personalfinancemanager.data.repository.FinanceRepository
import com.higashi.personalfinancemanager.ui.fragments.add_category.AddCategoryViewModel
import com.higashi.personalfinancemanager.ui.fragments.add_note.AddNoteViewModel
import com.higashi.personalfinancemanager.ui.fragments.all_categories.AllCategoriesViewModel
import com.higashi.personalfinancemanager.ui.fragments.choose_category.ChooseCategoryViewModel
import com.higashi.personalfinancemanager.ui.fragments.details_note.DetailsNoteViewModel
import com.higashi.personalfinancemanager.ui.fragments.diagrams.DiagramViewModel
import com.higashi.personalfinancemanager.ui.fragments.notes.NotesViewModel
import com.higashi.personalfinancemanager.ui.fragments.update_note.UpdateViewModel

class MainViewModelFactory(
    private val repository: FinanceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllCategoriesViewModel::class.java)) {
            return AllCategoriesViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(AddCategoryViewModel::class.java)) {
            return AddCategoryViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ChooseCategoryViewModel::class.java)) {
            return ChooseCategoryViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(AddNoteViewModel::class.java)) {
            return AddNoteViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetailsNoteViewModel::class.java)) {
            return DetailsNoteViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DiagramViewModel::class.java)) {
            return DiagramViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(UpdateViewModel::class.java)) {
            return UpdateViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}