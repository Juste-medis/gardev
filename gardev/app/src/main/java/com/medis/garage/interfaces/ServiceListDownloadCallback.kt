package com.medis.garage.interfaces

import com.medis.garage.pojo.ServiceClass
import java.util.ArrayList

interface ServiceListDownloadCallback {
    fun setServiceDetailDownloadCallback(serviceClassArrayList: ArrayList<ServiceClass>)
}