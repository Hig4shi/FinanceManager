package com.higashi.personalfinancemanager.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "transactions"
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val category_id: Long,
    val amount: Long,
    val description: String,
    val date: Long
) : Parcelable
