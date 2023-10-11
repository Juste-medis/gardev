package com.medis.garage.interfaces

import com.medis.garage.pojo.InsuranceClass
import java.util.ArrayList

interface InsuranceListDownloadCallback {
    fun setInsuranceDetailDownloadCallback(insuranceClassArrayList: ArrayList<InsuranceClass>)
}