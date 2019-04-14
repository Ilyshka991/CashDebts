package com.pechuro.cashdebts

/*DO not touch this
* if removed:
* com.pechuro.cashdebts.calculator.di.component.DaggerCalculatorComponent
* */
import com.pechuro.cashdebts.calculator.di.component.DaggerCalculatorComponent
import android.app.Activity
import android.app.Application
import com.pechuro.cashdebts.data.di.component.DaggerDataComponent
import com.pechuro.cashdebts.di.component.AppComponent
import com.pechuro.cashdebts.di.component.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)

        initDI()
    }

    override fun activityInjector() = activityDispatchingAndroidInjector

    private fun initDI() {
        val dataComponent = DaggerDataComponent.create()
        val calculatorComponent = DaggerCalculatorComponent.create()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .dataComponent(dataComponent)
            .calculatorComponent(calculatorComponent)
            .build()
        appComponent.inject(this)
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}