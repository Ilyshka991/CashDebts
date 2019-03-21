package com.pechuro.cashdebts.model.connectivity

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ConnectivityListener @Inject constructor(private val connectivityManager: ConnectivityManager) {

    private val emitter = BehaviorSubject.create<Boolean>()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            emitter.onNext(true)
        }

        override fun onUnavailable() {
            emitter.onNext(false)
        }

        override fun onLost(network: Network?) {
            emitter.onNext(false)
        }
    }

    init {
        initService()
    }

    fun listen(onChange: (Boolean) -> Unit) = emitter
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onChange)

    private fun initService() {
        val request = NetworkRequest.Builder().addCapability(NET_CAPABILITY_INTERNET).build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }
}