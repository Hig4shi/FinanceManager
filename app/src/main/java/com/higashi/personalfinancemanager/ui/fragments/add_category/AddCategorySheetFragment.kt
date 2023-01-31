package com.higashi.personalfinancemanager.ui.fragments.add_category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.higashi.personalfinancemanager.R
import com.higashi.personalfinancemanager.data.FinanceManagerDatabase
import com.higashi.personalfinancemanager.data.entities.Category
import com.higashi.personalfinancemanager.databinding.FragmentAddCategorySheetBinding
import com.higashi.personalfinancemanager.data.repository.FinanceRepository
import com.higashi.personalfinancemanager.ui.MainViewModelFactory

class AddCategorySheetFragment: BottomSheetDialogFragment(R.layout.fragment_add_category_sheet) {

    private lateinit var binding: FragmentAddCategorySheetBinding
    private lateinit var categoryViewModel: AddCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCategorySheetBinding.inflate(inflater, container, false)

        val repository = FinanceRepository(FinanceManagerDatabase(requireContext()))
        val viewModelFactory = MainViewModelFactory(repository)
        categoryViewModel = ViewModelProvider(this, viewModelFactory)[AddCategoryViewModel::class.java]

        binding.expense.setOnClickListener {
            with(binding) {
                enterCategoryEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_red))
                nameTextInputLayout.boxStrokeColor = ContextCompat.getColor(requireContext(), R.color.card_red)
                descriptionTextInputLayout.boxStrokeColor = ContextCompat.getColor(requireContext(), R.color.card_red)
                prefixChooseTypeEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_red))
                addCategoryButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card_red))
            }
        }

        binding.revenue.setOnClickListener {
            with(binding) {
                enterCategoryEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_green))
                nameTextInputLayout.boxStrokeColor = ContextCompat.getColor(requireContext(), R.color.card_green)
                descriptionTextInputLayout.boxStrokeColor = ContextCompat.getColor(requireContext(), R.color.card_green)
                prefixChooseTypeEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_green))
                addCategoryButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card_green))
            }
        }

        binding.addCategoryButton.setOnClickListener {
            insertCategory()
        }

        return binding.root
    }

    private fun insertCategory() {
        val name = binding.categoryNameEditText.text.toString()
        val description = binding.categoryDescriptionEditText.text.toString()
        val selectedButton = binding.typeOfOperation.selectedButtons
        val typeOfOperation = if (selectedButton.size != 0) selectedButton[0].text else ""

        if (isntEmpty(name, description, typeOfOperation)) {
            val category = Category(0, name, description, typeOfOperation)
            categoryViewModel.insertCategory(category)
            Toast.makeText(requireContext(), "Category was successfully added!", Toast.LENGTH_LONG).show()
            this@AddCategorySheetFragment.dismiss()
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG).show()
        }
    }

    private fun isntEmpty(name: String, description: String, typeOfOperation: String): Boolean {
        return (name.isNotBlank() && description.isNotBlank() && typeOfOperation.isNotBlank())
    }
}