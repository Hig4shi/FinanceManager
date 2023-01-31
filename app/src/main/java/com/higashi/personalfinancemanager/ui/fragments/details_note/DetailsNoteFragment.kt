package com.higashi.personalfinancemanager.ui.fragments.details_note

import android.app.Dialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.higashi.personalfinancemanager.R
import com.higashi.personalfinancemanager.data.FinanceManagerDatabase
import com.higashi.personalfinancemanager.data.entities.TransactionAndCategory
import com.higashi.personalfinancemanager.data.repository.FinanceRepository
import com.higashi.personalfinancemanager.databinding.FragmentDetailsNoteBinding
import com.higashi.personalfinancemanager.ui.MainViewModelFactory
import com.higashi.personalfinancemanager.ui.fragments.choose_category.ChooseCategoryFragmentDirections
import kotlinx.android.synthetic.main.fragment_details_note.*
import java.util.*

class DetailsNoteFragment : BottomSheetDialogFragment(R.layout.fragment_details_note) {

    private lateinit var binding: FragmentDetailsNoteBinding
    private val args by navArgs<DetailsNoteFragmentArgs>()
    private var dateFormater = SimpleDateFormat("dd/MM/yyyy")
    private lateinit var detailsNoteViewModel: DetailsNoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsNoteBinding.inflate(inflater, container, false)

        with((activity as AppCompatActivity)) {
            setSupportActionBar(binding.transactionDetailsToolbar)
            supportActionBar?.elevation = 0f
        }

        with(binding.transactionDetailsToolbar) {
            setNavigationIcon(R.drawable.ic_on_back_pressed)
            setNavigationOnClickListener {
                this@DetailsNoteFragment.dismiss()
            }
        }

        val repository = FinanceRepository(FinanceManagerDatabase(requireContext()))
        val viewModelFactory = MainViewModelFactory(repository)
        detailsNoteViewModel = ViewModelProvider(this, viewModelFactory)[DetailsNoteViewModel::class.java]


        val transactionWithCategory = args.transactionWithCategory
        detailsNoteViewModel.getTransactionById(transactionWithCategory.id).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            with(binding) {
                categoryNameTextView.text = transactionWithCategory.name
                typeOfOperationTextView.text = transactionWithCategory.type_of_operation
                if (transactionWithCategory.type_of_operation == requireContext().getString(R.string.expense)) {
                    amountOfMoneyTextView.text = requireContext().getString(R.string.minusMoney, it.amount.toString())
                } else {
                    amountOfMoneyTextView.text = requireContext().getString(R.string.plusMoney, it.amount.toString())
                }
                dateTextView.text = dateFormater.format(Date(it.date)).toString()
                descriptionTextView.text = it.description
            }
        })

        binding.deleteTransactionButton.setOnClickListener {
            detailsNoteViewModel.deleteTransaction(transactionWithCategory.id)
            this@DetailsNoteFragment.dismiss()
        }

        binding.updateTransactionDetailsButton.setOnClickListener{
            val action = DetailsNoteFragmentDirections.actionDetailsNoteFragmentToUpdateNoteFragment(transactionWithCategory)
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

}