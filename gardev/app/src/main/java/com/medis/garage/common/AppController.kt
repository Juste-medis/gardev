package com.medis.garage.common

import android.app.Application

class AppController : Application() {

    companion object {
        var BaseUrl = ""
    }


    override fun onCreate() {
        super.onCreate()
        BaseUrl = "http://benzatineinfotech.com/webservice/vehicle_app/index.php/vehicle_api/"
    }
}