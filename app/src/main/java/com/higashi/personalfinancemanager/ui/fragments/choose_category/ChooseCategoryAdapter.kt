package com.higashi.personalfinancemanager.ui.fragments.choose_category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.higashi.personalfinancemanager.R
import com.higashi.personalfinancemanager.data.entities.Category
import kotlinx.android.synthetic.main.fragment_add_category_sheet.view.*
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_category.view.typeOfOperation

class ChooseCategoryAdapter() : RecyclerView.Adapter<ChooseCategoryAdapter.CategoriesViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_category,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = differ.currentList[position]
        holder.itemView.apply {
            categoryNameTextView.text = category.name
            categoryDescriptionTextView.text = category.description
            typeOfOperation.text = category.type_of_operation
            with(typeOfOperation) {
                if (category.type_of_operation == "Expense") {
                    typeOfOperation.setTextColor(ContextCompat.getColor(context, R.color.card_red))
                } else {
                    typeOfOperation.setTextColor(ContextCompat.getColor(context, R.color.card_green))
                }
            }
            setOnClickListener {
                onItemClickListener?.let { it(category) }
            }
        }
    }

    private var onItemClickListener: ((Category) -> Unit)? = null

    fun setOnItemClickListener(listener: (Category) -> Unit) {
        onItemClickListener = listener
    }

    class CategoriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}
