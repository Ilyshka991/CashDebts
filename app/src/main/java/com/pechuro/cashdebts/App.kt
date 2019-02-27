package com.pechuro.cashdebts

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.pechuro.cashdebts.di.component.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        /*if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)*/

        initDI()
        initFirebase()
    }

    override fun activityInjector() = activityDispatchingAndroidInjector

    private fun initDI() {
        DaggerAppComponent.builder().application(this).build().inject(this)
    }

    private fun initFirebase() {
        //  firebaseInteractor.init()
    }
}