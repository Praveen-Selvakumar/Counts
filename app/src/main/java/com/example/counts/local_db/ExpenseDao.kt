package com.example.counts.local_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addExpense(model : CountModel)

    @Update
    fun UpdateExpense(model : CountModel)

    @Query("SELECT * FROM count")
    fun  getExpense():  List<CountModel>

    @Query("DELETE FROM COUNT WHERE id = :id")
    fun deleteSpecificExpense(id : Int)

}