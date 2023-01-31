package com.higashi.personalfinancemanager.data.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionAndCategory(
    val category_id: Long,
    val name: String,
    val type_of_operation: String,
    val id: Long,
    val description: String,
    val amount: Long,
    val date: Long
) : Parcelable