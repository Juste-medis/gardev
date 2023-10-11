package com.medis.garage.interfaces

import com.medis.garage.pojo.VehicleClass
import java.util.ArrayList

interface VehicleListDownloadCallback {
    fun setVehicleDetailDownloadCallback(vehicleClassArrayList: ArrayList<VehicleClass>)
}