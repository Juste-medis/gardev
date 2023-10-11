package com.medis.garage.interfaces

import com.medis.garage.pojo.PUCClass

interface PUCListDownloadCallback {
    fun setPUCDetailDownloadCallback(pucClassArrayList: ArrayList<PUCClass>)
}