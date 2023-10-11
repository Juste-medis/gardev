package com.medis.garage.adapters

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.medis.garage.R
import com.medis.garage.common.CommonUtilities
import com.medis.garage.interfaces.AdapterItemCallback
import com.medis.garage.pojo.AccidentClass
import java.util.ArrayList

class AccidentListAdapter(private val context: Context,
                          private val accidentClassArrayList: ArrayList<AccidentClass>,
                          private val adapterItemCallback: AdapterItemCallback) : androidx.recyclerview.widget.RecyclerView.Adapter<AccidentListAdapter.AdapterViewHolder>() {

    inner class AdapterViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view), View.OnClickListener {

        val llMain = view.findViewById(R.id.llMain) as LinearLayout
        val tvMonth = view.findViewById(R.id.tvMonth) as AppCompatTextView
        val tvDate = view.findViewById(R.id.tvDate) as AppCompatTextView
        val tvYear = view.findViewById(R.id.tvYear) as AppCompatTextView
        val tvAmount = view.findViewById(R.id.tvAmount) as AppCompatTextView
        val tvKm  = view.findViewById(R.id.tvKm) as AppCompatTextView

        init {
            llMain.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val id = p0!!.id
            if (id == R.id.llMain) {
                adapterItemCallback.onItemTypeClickCallback(p0.tag as Int)
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdapterViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.cell_accident_list, p0, false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(p0: AdapterViewHolder, p1: Int) {
        val aClass = accidentClassArrayList[p1]

        val dateData = CommonUtilities.getDateWithMonthName(aClass.accidentDate).split("-")

        try {
            p0.tvMonth.text = dateData[0]
            p0.tvDate.text = dateData[1]
            p0.tvYear.text = dateData[2]
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            p0.tvAmount.text = aClass.accidentAmount
            p0.tvKm.text = aClass.accidentKmReading
        } catch (e: Exception) {
            e.printStackTrace()
        }

        p0.llMain.tag = p1
    }

    override fun getItemCount(): Int {
        return accidentClassArrayList.size
    }
}