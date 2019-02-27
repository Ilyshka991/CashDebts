package com.pechuro.cashdebts

import android.app.Activity
import android.app.Application
import com.pechuro.cashdebts.di.component.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        /*if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)*/

        initDI()
    }

    override fun activityInjector() = activityDispatchingAndroidInjector

    private fun initDI() {
        DaggerAppComponent.builder().application(this).build().inject(this)
    }
}