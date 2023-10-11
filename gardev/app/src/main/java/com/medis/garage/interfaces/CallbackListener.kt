package com.medis.garage.interfaces

interface CallbackListener {

    fun onSuccess()

    fun onCancel()

    fun onRetry()
}