package com.medis.garage.interfaces

import com.medis.garage.pojo.RefuelClass
import java.util.ArrayList

interface RefuelListDownloadCallback {
    fun setRefuelDetailDownloadCallback(refuelClassArrayList: ArrayList<RefuelClass>)
}