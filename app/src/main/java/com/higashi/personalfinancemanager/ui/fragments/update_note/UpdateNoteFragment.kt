package com.higashi.personalfinancemanager.ui.fragments.update_note

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
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
import com.higashi.personalfinancemanager.databinding.FragmentUpdateNoteBinding
import com.higashi.personalfinancemanager.ui.MainViewModelFactory
import com.higashi.personalfinancemanager.ui.fragments.details_note.DetailsNoteFragmentDirections
import kotlinx.android.synthetic.main.fragment_add_note_sheet.*
import java.util.*

class UpdateNoteFragment : BottomSheetDialogFragment(R.layout.fragment_update_note) {

    private lateinit var binding: FragmentUpdateNoteBinding
    private val args by navArgs<UpdateNoteFragmentArgs>()
    private lateinit var updateViewModel: UpdateViewModel
    private var dateFormater = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)

        val repository = FinanceRepository(FinanceManagerDatabase(requireContext()))
        val viewModelFactory = MainViewModelFactory(repository)
        updateViewModel = ViewModelProvider(this, viewModelFactory)[UpdateViewModel::class.java]

        with(binding.updateToolbar) {
            setNavigationIcon(R.drawable.ic_on_back_pressed)
            setNavigationOnClickListener {
                this@UpdateNoteFragment.dismiss()
            }
        }

        val oldTransaction = args.oldTransaction
        binding.categoryNameEditText.setText(oldTransaction.name)
        if (oldTransaction.type_of_operation == "Expense") {
            with(binding.expenseTextView) {
                setBackgroundResource(R.drawable.background_operation_expense)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
            with(binding) {
                categoryNameEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_red))
                amountOfMoneyEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_red))
                descriptionEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_red))
                updateToolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card_red))
                updateTransactionButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card_red))
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
                updateToolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card_green))
                updateTransactionButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.card_green))
            }
        }

        with(binding) {
            amountOfMoneyEditText.setText(oldTransaction.amount.toString())
            descriptionEditText.setText(oldTransaction.description)
            datePicker.text = dateFormater.format(oldTransaction.date)
        }

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

        binding.updateTransactionButton.setOnClickListener {
            updateTransaction()
        }

        return binding.root
    }

    private fun updateTransaction() {
        val amount = binding.amountOfMoneyEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val dateInMill = dateFormater.parse(binding.datePicker.text.toString()).time.toString()

        if (isntEmpty(amount, description, dateInMill)) {
            val transaction = Transaction(args.oldTransaction.id, args.oldTransaction.category_id, amount.toLong(), description, dateInMill.toLong())
            updateViewModel.updateTransaction(transaction)
            Toast.makeText(requireContext(), "Transaction was successfully updated!", Toast.LENGTH_LONG).show()
            this@UpdateNoteFragment.dismiss()
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG).show()
        }
    }

    private fun isntEmpty(amount: String, description: String, dateInMill: String): Boolean {
        return (amount.isNotBlank() && description.isNotBlank() && dateInMill.isNotBlank() && args.oldTransaction.id != null)
    }

}