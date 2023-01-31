package com.higashi.personalfinancemanager.ui.fragments.all_categories

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.higashi.personalfinancemanager.R
import com.higashi.personalfinancemanager.data.FinanceManagerDatabase
import com.higashi.personalfinancemanager.databinding.FragmentAllCategoriesSheetBinding
import com.higashi.personalfinancemanager.data.repository.FinanceRepository
import com.higashi.personalfinancemanager.ui.MainViewModelFactory

class AllCategoriesSheetFragment: BottomSheetDialogFragment(R.layout.fragment_all_categories_sheet) {

    private lateinit var binding: FragmentAllCategoriesSheetBinding
    private lateinit var categoriesAdapter: AllCategoriesAdapter
    private lateinit var categoriesViewModel: AllCategoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllCategoriesSheetBinding.inflate(inflater, container, false)

        with(binding.allCategoriesToolbar) {
            setNavigationIcon(R.drawable.ic_on_back_pressed)
            setNavigationOnClickListener {
                this@AllCategoriesSheetFragment.dismiss()
            }
        }

        binding.createCategoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_allCategoriesSheetFragment_to_addCategorySheetFragment)
            //AddCategorySheetFragment().show(parentFragmentManager, "AddCategoryTag")
        }

        val repository = FinanceRepository(FinanceManagerDatabase(requireContext()))
        val viewModelFactory = MainViewModelFactory(repository)
        categoriesViewModel = ViewModelProvider(this, viewModelFactory)[AllCategoriesViewModel::class.java]

        setupRecyclerView()

        categoriesViewModel.getAllCategories().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.differ.submitList(categories)
//            if (categoriesAdapter.differ.currentList.isEmpty()) {
//                binding.noDataImageView.visibility = View.VISIBLE
//                binding.noDataTextView.visibility = View.VISIBLE
//            } else {
//                binding.noDataImageView.visibility = View.GONE
//                binding.noDataTextView.visibility = View.GONE
//            }
        })

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
                val category = categoriesAdapter.differ.currentList[position]
                categoriesViewModel.deleteCategory(category)
                Snackbar.make(requireView(), "Category was successfully deleted!", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        categoriesViewModel.insertCategory(category)
                    }
                    show()
                }

            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.allCategoriesRecyclerView)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        categoriesAdapter = AllCategoriesAdapter()
        binding.allCategoriesRecyclerView.apply {
            adapter = categoriesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

}