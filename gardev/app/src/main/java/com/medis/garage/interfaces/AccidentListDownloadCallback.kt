package com.medis.garage.interfaces

import com.medis.garage.pojo.AccidentClass

interface AccidentListDownloadCallback {
    fun setAccidentDetailDownloadCallback(accidentClassArrayList: ArrayList<AccidentClass>)
}