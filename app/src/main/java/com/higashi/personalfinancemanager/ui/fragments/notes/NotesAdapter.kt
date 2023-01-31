package com.higashi.personalfinancemanager.ui.fragments.notes

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.higashi.personalfinancemanager.R
import com.higashi.personalfinancemanager.data.entities.TransactionAndCategory
import kotlinx.android.synthetic.main.item_note.view.*
import java.util.*

class NotesAdapter() : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private var dateFormater = SimpleDateFormat("dd/MM/yyyy")

    private val differCallback = object : DiffUtil.ItemCallback<TransactionAndCategory>() {
        override fun areItemsTheSame(oldItem: TransactionAndCategory, newItem: TransactionAndCategory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TransactionAndCategory, newItem: TransactionAndCategory): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_note,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val transactionAndCategory = differ.currentList[position]
        holder.itemView.apply {
            noteNameTextView.text = transactionAndCategory.name
            dateOfOperation.text = dateFormater.format(Date(transactionAndCategory.date))
            with(amountOfMoneyTextView) {
                if (transactionAndCategory.type_of_operation == context.getString(R.string.expense)) {
                    setTextColor(ContextCompat.getColor(context, R.color.card_red))
                    text = context.getString(R.string.minusMoney, transactionAndCategory.amount.toString())
                } else {
                    setTextColor(ContextCompat.getColor(context, R.color.card_green))
                    text = context.getString(R.string.plusMoney, transactionAndCategory.amount.toString())
                }
            }
            setOnClickListener {
                onItemClickListener?.let { it(transactionAndCategory) }
            }
        }
    }

    private var onItemClickListener: ((TransactionAndCategory) -> Unit)? = null

    fun setOnItemClickListener(listener: (TransactionAndCategory) -> Unit) {
        onItemClickListener = listener
    }

    class NotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}
