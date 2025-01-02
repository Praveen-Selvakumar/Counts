package com.example.counts.local_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.counts.objects.DateTimeUtils
import java.util.Calendar


@Entity(tableName = "COUNT")
data class CountModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "time")
    var currentDate: String  = DateTimeUtils().getFullDateAndTime(),

    @ColumnInfo(name = "amount")
    var amount: Float = 0.0f,

    @ColumnInfo(name = "why")
    var why: String = "",

    @ColumnInfo(name = "origin")
    var origin: String = "",

    @ColumnInfo(name = "action")
    var action: Int = 0
)