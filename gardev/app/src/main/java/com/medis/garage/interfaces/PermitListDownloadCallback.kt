package com.medis.garage.interfaces

import com.medis.garage.pojo.PermitClass

interface PermitListDownloadCallback {
    fun setPermitDetailDownloadCallback(permitClassArrayList: ArrayList<PermitClass>)
}