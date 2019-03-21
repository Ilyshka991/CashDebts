package com.pechuro.cashdebts

import android.app.Activity
import android.app.Application
import com.pechuro.cashdebts.di.component.AppComponent
import com.pechuro.cashdebts.di.component.DaggerAppComponent
import com.pechuro.cashdebts.di.component.DaggerDataComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    override fun activityInjector() = activityDispatchingAndroidInjector

    private fun initDI() {
        val dataComponent = DaggerDataComponent.create()
        appComponent = DaggerAppComponent.builder().application(this).dataComponent(dataComponent).build()
        appComponent.inject(this)
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}