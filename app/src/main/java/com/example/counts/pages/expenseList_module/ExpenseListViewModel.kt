package com.example.counts.pages.expenseList_module

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.counts.local_db.CountModel
import com.example.counts.local_db.ExpenseDao

class ExpenseListViewModel : ViewModel() {
    fun getExpenseList(dao: ExpenseDao): MutableLiveData<List<CountModel>> {
        return MutableLiveData(dao.getExpense())
    }

}