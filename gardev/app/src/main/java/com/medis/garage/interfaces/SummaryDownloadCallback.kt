package com.medis.garage.interfaces

import com.medis.garage.pojo.SummaryClass

interface SummaryDownloadCallback {
    fun setSummaryDetailDownloadCallback(summaryClassArrayList : ArrayList<SummaryClass>)
}