package com.medis.garage.interfaces

import com.medis.garage.pojo.ExpenseClass
import java.util.ArrayList

interface ExpenseListDownloadCallback {
    fun setExpenseDetailDownloadCallback(expenseClassArrayList: ArrayList<ExpenseClass>)
}