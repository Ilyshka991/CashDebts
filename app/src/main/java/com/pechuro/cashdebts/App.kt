package com.pechuro.cashdebts

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.pechuro.cashdebts.data.remote.FirestoreRepository
import com.pechuro.cashdebts.di.component.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var firestoreRepository: FirestoreRepository

    override fun onCreate() {
        super.onCreate()

        /*if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)*/

        initDI()
        firestoreRepository.startSync()
    }

    override fun activityInjector() = activityDispatchingAndroidInjector

    private fun initDI() {
        DaggerAppComponent.builder().application(this).build().inject(this)
    }
}