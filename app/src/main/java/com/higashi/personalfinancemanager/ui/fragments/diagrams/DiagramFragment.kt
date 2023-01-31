package com.higashi.personalfinancemanager.ui.fragments.diagrams

import android.app.DatePickerDialog
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.higashi.personalfinancemanager.R
import com.higashi.personalfinancemanager.data.FinanceManagerDatabase
import com.higashi.personalfinancemanager.data.repository.FinanceRepository
import com.higashi.personalfinancemanager.databinding.FragmentDiagramBinding
import com.higashi.personalfinancemanager.ui.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_note_sheet.*
import kotlinx.android.synthetic.main.fragment_diagram.*
import java.util.*
import kotlin.collections.ArrayList

class DiagramFragment : Fragment(R.layout.fragment_diagram) {

    private lateinit var binding: FragmentDiagramBinding
    private lateinit var diagramViewModel: DiagramViewModel
    private val list: ArrayList<PieEntry> = ArrayList()
    private var dateFormater = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiagramBinding.inflate(inflater, container, false)

        with((activity as AppCompatActivity)) {
            setSupportActionBar(binding.diagramToolbar)
            supportActionBar?.elevation = 0f
        }

        val repository = FinanceRepository(FinanceManagerDatabase(requireContext()))
        val viewModelFactory = MainViewModelFactory(repository)
        diagramViewModel = ViewModelProvider(this, viewModelFactory)[DiagramViewModel::class.java]

        binding.expensesThemedButton.setOnClickListener {
            getDateAndSetupDiagram(binding.expensesThemedButton.text)
        }

        binding.revenuesThemedButton.setOnClickListener {
            getDateAndSetupDiagram(binding.revenuesThemedButton.text)
        }

        with(binding) {
            startDatePicker.text = dateFormater.format(Date().time)
            endDatePicker.text = dateFormater.format(Date().time)
        }



        binding.startDatePicker.setOnClickListener {
            if (binding.typeOfOperation.selectedButtons.size == 0) {
                Toast.makeText(requireContext(), "Choose type of operation!", Toast.LENGTH_LONG).show()
            } else {
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
                        val startDate = selectedDate.time
                        val endDate = dateFormater.parse(binding.endDatePicker.text.toString()).time
                        if (startDate.time > endDate) {
                            Toast.makeText(requireContext(), "Start date can't be greater than end date!", Toast.LENGTH_LONG).show()
                        } else {
                            binding.startDatePicker.text = dateFormater.format(startDate)
                            getDiagramData(binding.typeOfOperation.selectedButtons[0]?.let {it.text}, startDate.time, endDate)
                        }
                    }, getDate.get(Calendar.YEAR), getDate.get(Calendar.MONTH), getDate.get(Calendar.DAY_OF_MONTH))
                datePicker.show()
            }
        }

        binding.endDatePicker.setOnClickListener {
            if (binding.typeOfOperation.selectedButtons.size == 0) {
                Toast.makeText(requireContext(), "Choose type of operation!", Toast.LENGTH_LONG).show()
            } else {
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
                        val startDate = dateFormater.parse(binding.endDatePicker.text.toString()).time
                        val endDate = selectedDate.time
                        if (endDate.time < startDate) {
                            Toast.makeText(requireContext(), "End date can't be lower than start date!", Toast.LENGTH_LONG).show()
                        } else {
                            binding.endDatePicker.text = dateFormater.format(endDate)
                            getDiagramData(binding.typeOfOperation.selectedButtons[0].text, startDate, endDate.time)
                        }
                    }, getDate.get(Calendar.YEAR), getDate.get(Calendar.MONTH), getDate.get(Calendar.DAY_OF_MONTH))
                datePicker.show()
            }
        }

        getDateAndSetupDiagram(requireContext().getString(R.string.expenses))

        return binding.root
    }

    private fun getDateAndSetupDiagram(typeOfOperation: String) {
        val startDate = dateFormater.parse(binding.startDatePicker.text.toString()).time
        val endDate = dateFormater.parse(binding.endDatePicker.text.toString()).time
        getDiagramData(typeOfOperation, startDate, endDate)
    }

    private fun getDiagramData(typeOfOperation: String, startDate: Long, endDate: Long) {
        list.clear()
        val type = if (typeOfOperation == requireContext().getString(R.string.expenses))
            requireContext().getString(R.string.expense) else requireContext().getString(R.string.revenue)
        diagramViewModel.getDataForDiagram(type, startDate, endDate).observe(viewLifecycleOwner, Observer { listOfData ->
            for (data in listOfData) {
                list.add(PieEntry(data.amount.toFloat(), data.name))
            }
            val pieDataSet = PieDataSet(list, "$typeOfOperation data")

            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
            pieDataSet.valueTextColor = requireContext().getColor(R.color.black)
            pieDataSet.valueTextSize = 15f

            with(binding.pie) {
                data = PieData(pieDataSet)
                description.text = "$typeOfOperation Pie Chart"
                centerText = "$typeOfOperation data"
                animateY(1200)
                visibility = View.VISIBLE
            }
        })
    }

}