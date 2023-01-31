package com.higashi.personalfinancemanager.ui.fragments.choose_category

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.higashi.personalfinancemanager.R
import com.higashi.personalfinancemanager.data.FinanceManagerDatabase
import com.higashi.personalfinancemanager.data.repository.FinanceRepository
import com.higashi.personalfinancemanager.databinding.FragmentChooseCategoryBinding
import com.higashi.personalfinancemanager.ui.MainViewModelFactory
import com.higashi.personalfinancemanager.ui.fragments.all_categories.AllCategoriesAdapter
import com.higashi.personalfinancemanager.ui.fragments.all_categories.AllCategoriesViewModel

class ChooseCategoryFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentChooseCategoryBinding
    private lateinit var categoriesAdapter: ChooseCategoryAdapter
    private lateinit var categoriesViewModel: ChooseCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseCategoryBinding.inflate(inflater, container, false)

        with(binding.allCategoriesToolbar) {
            setNavigationIcon(R.drawable.ic_on_back_pressed)
            setNavigationOnClickListener {
                this@ChooseCategoryFragment.dismiss()
            }
        }

        val repository = FinanceRepository(FinanceManagerDatabase(requireContext()))
        val viewModelFactory = MainViewModelFactory(repository)
        categoriesViewModel = ViewModelProvider(this, viewModelFactory)[ChooseCategoryViewModel::class.java]

        setupRecyclerView()

        categoriesViewModel.getAllCategories().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.differ.submitList(categories)
            if (categoriesAdapter.differ.currentList.isEmpty()) {
                binding.noDataImageView.visibility = View.VISIBLE
                binding.noDataTextView.visibility = View.VISIBLE
            } else {
                binding.noDataImageView.visibility = View.GONE
                binding.noDataTextView.visibility = View.GONE
            }
        })

        categoriesAdapter.setOnItemClickListener {
            val action = ChooseCategoryFragmentDirections.actionChooseCategoryFragmentToAddNoteFragment(it)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        categoriesAdapter = ChooseCategoryAdapter()
        binding.allCategoriesRecyclerView.apply {
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}