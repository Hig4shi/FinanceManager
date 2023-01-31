package com.higashi.personalfinancemanager.ui.fragments.add_note

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.higashi.personalfinancemanager.R
import com.higashi.personalfinancemanager.data.FinanceManagerDatabase
import com.higashi.personalfinancemanager.data.entities.Transaction
import com.higashi.personalfinancemanager.data.repository.FinanceRepository
import com.higashi.personalfinancemanager.databinding.FragmentAddNoteSheetBinding
import com.higashi.personalfinancemanager.ui.MainViewModelFactory
import com.higashi.personalfinancemanager.ui.fragments.choose_category.ChooseCategoryFragment
import com.higashi.personalfinancemanager.ui.fragments.notes.NotesViewModel
import java.util.*

class AddNoteFragment: BottomSheetDialogFragment(R.layout.fragment_add_note_sheet) {

    private lateinit var binding: FragmentAddNoteSheetBinding
    private val args by navArgs<AddNoteFragmentArgs>()
    private var dateFormater = SimpleDateFormat("dd/MM/yyyy")
    private lateinit var addNoteViewModel: AddNoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNoteSheetBinding.inflate(inflater, container, false)

        dateFormater.isLenient = false

        val repository = FinanceRepository(FinanceManagerDatabase(requireContext()))
        val viewModelFactory = MainViewModelFactory(repository)
        addNoteViewModel = ViewModelProvider(this, viewModelFactory)[AddNoteViewModel::class.java]

        with(binding.categoriesToolbar) {
            setNavigationIcon(R.drawable.ic_on_back_pressed)
            setNavigationOnClickListener {
                this@AddNoteFragment.dismiss()
            }
        }

        val chosenCategory = args.chosenCategory
        binding.categoryNameEditText.setText(chosenCategory.name)
        if (chosenCategory.type_of_operation == "Expense") {
            with(binding.expenseTextView) {
                setBackgroundResource(R.drawable.background_operation_expense)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
            with(binding) {
                categoryNameEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_red))
                amountOfMoneyEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_red))
                descriptionEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_red))
                categoriesToolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card_red))
                addTransactionButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card_red))
            }
        } else {
            with(binding.revenueTextView) {
                setBackgroundResource(R.drawable.background_operation_revenue)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
            with(binding) {
                categoryNameEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_green))
                amountOfMoneyEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_green))
                descriptionEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_green))
                categoriesToolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card_green))
                addTransactionButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card_green))
            }
        }

        binding.datePicker.text = dateFormater.format(Date().time)

        binding.datePicker.setOnClickListener {
            val getDate = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                R.style.customDatePickerStyle,
                DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
                    val selectedDate = Calendar.getInstance()
                    with(selectedDate) {
                        set(Calendar.YEAR, i)
                        set(Calendar.MONTH, i2)
                        set(Calendar.DAY_OF_MONTH, i3)
                    }
                    val date = dateFormater.format(selectedDate.time)
                    binding.datePicker.text = date
                }, getDate.get(Calendar.YEAR), getDate.get(Calendar.MONTH), getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        binding.addTransactionButton.setOnClickListener {
            insertTransaction()
        }

        return binding.root
    }

    private fun insertTransaction() {
        val amount = binding.amountOfMoneyEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val dateInMill = dateFormater.parse(binding.datePicker.text.toString()).time.toString()

        if (isntEmpty(amount, description, dateInMill)) {
            val transaction = Transaction(0, args.chosenCategory.id, amount.toLong(), description, dateInMill.toLong())
            addNoteViewModel.insertTransaction(transaction)
            Toast.makeText(requireContext(), "Transaction was successfully created!", Toast.LENGTH_LONG).show()
            this@AddNoteFragment.dismiss()
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG).show()
        }
    }

    private fun isntEmpty(amount: String, description: String, dateInMill: String): Boolean {
        return (amount.isNotBlank() && description.isNotBlank() && dateInMill.isNotBlank() && args.chosenCategory.id != null)
    }

}