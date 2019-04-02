package com.pechuro.cashdebts.model.connectivity

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ConnectivityListener @Inject constructor(private val connectivityManager: ConnectivityManager) {

    private val emitter = BehaviorSubject.create<Boolean>()
    private val isConnectionAvailable: Boolean
        get() = connectivityManager.activeNetworkInfo?.isConnected ?: false

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            emitter.onNext(true)
        }

        override fun onUnavailable() {
            if (!isConnectionAvailable) emitter.onNext(false)
        }

        override fun onLost(network: Network?) {
            if (!isConnectionAvailable) emitter.onNext(false)
        }
    }

    init {
        initService()
    }

    fun listen(onChange: (Boolean) -> Unit) = emitter
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onChange)

    fun listen(observer: Observer<Boolean>) = emitter
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer)

    private fun initService() {
        val request =
            NetworkRequest.Builder().addCapability(NET_CAPABILITY_INTERNET).build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
        emitter.onNext(isConnectionAvailable)
    }
}