package com.pechuro.cashdebts.ui.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    //  protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        //  compositeDisposable.dispose()
        super.onCleared()
    }
}
