package com.higashi.personalfinancemanager.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.higashi.personalfinancemanager.R
import com.higashi.personalfinancemanager.databinding.FragmentProfileBinding
import com.higashi.personalfinancemanager.ui.MainActivity
import com.higashi.personalfinancemanager.ui.fragments.add_category.AddCategorySheetFragment
import com.higashi.personalfinancemanager.ui.fragments.all_categories.AllCategoriesSheetFragment

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.configureCategoriesButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_allCategoriesSheetFragment)
            //AllCategoriesSheetFragment().show(parentFragmentManager, "AllCategoriesTag")
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onPause() {
        super.onPause()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}