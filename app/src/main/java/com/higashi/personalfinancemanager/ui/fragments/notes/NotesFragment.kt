package com.higashi.personalfinancemanager.ui.fragments.notes

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.higashi.personalfinancemanager.R
import com.higashi.personalfinancemanager.data.FinanceManagerDatabase
import com.higashi.personalfinancemanager.data.repository.FinanceRepository
import com.higashi.personalfinancemanager.databinding.FragmentNotesBinding
import com.higashi.personalfinancemanager.ui.MainViewModelFactory
import java.util.*


class NotesFragment : Fragment(R.layout.fragment_notes) {

    private lateinit var binding: FragmentNotesBinding
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var notesViewModel: NotesViewModel
    private var dateFormater = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(inflater, container, false)

        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val currentDate = cal.timeInMillis

        (activity as AppCompatActivity).setSupportActionBar(binding.notesToolbar)

        val repository = FinanceRepository(FinanceManagerDatabase(requireContext()))
        val viewModelFactory = MainViewModelFactory(repository)
        notesViewModel = ViewModelProvider(this, viewModelFactory)[NotesViewModel::class.java]

        setupRecyclerView()

        getTransactionsByDate(currentDate, Date().time + MONTH_IN_MILLIS)

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val transactionId = notesAdapter.differ.currentList[position].id
                notesViewModel.deleteTransaction(transactionId)
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.allTransactionsRecyclerView)
        }

        notesAdapter.setOnItemClickListener {
            val action = NotesFragmentDirections.actionNotesFragmentToDetailsNoteFragment(it)
            findNavController().navigate(action)
        }

        binding.dateRangeImageView.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            datePicker.show(parentFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                val startDate = datePicker.selection?.first!!
                val endDate = datePicker.selection?.second!!
                getTransactionsByDate(startDate, endDate)
            }

            datePicker.addOnNegativeButtonClickListener {
            }

            datePicker.addOnCancelListener {
            }
        }

        return binding.root
    }

    private fun getTransactionsByDate(startDate: Long, endDate: Long) {
        notesViewModel.getTransactionsAndCategories(startDate, endDate).observe(viewLifecycleOwner, Observer { transactionsAndCategories ->
            notesAdapter.differ.submitList(transactionsAndCategories)

        })

        notesViewModel.getTotalQuantity(requireContext().getString(R.string.expense), startDate, endDate).observe(viewLifecycleOwner, Observer { expenseSum ->
            binding.expensesTextView.text = (expenseSum?.let { it } ?: 0 ).toString()
        })

        notesViewModel.getTotalQuantity(requireContext().getString(R.string.revenue), startDate, endDate).observe(viewLifecycleOwner, Observer { revenueSum ->
            binding.revenuesTextView.text = (revenueSum?.let { it } ?: 0 ).toString()
        })

        notesViewModel.getBalance(startDate, endDate).observe(viewLifecycleOwner, Observer { balance ->
            binding.balanceTextView.text = (balance?.let { it } ?: 0 ).toString()
        })
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter()
        binding.allTransactionsRecyclerView.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    companion object {
        private const val MONTH_IN_MILLIS = 2629800000
    }

}