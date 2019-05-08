package com.pechuro.cashdebts

/*DO not touch this
* if removed:
* com.pechuro.cashdebts.calculator.di.component.DaggerCalculatorComponent
* */
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Service
import androidx.core.content.pm.PackageInfoCompat
import com.pechuro.cashdebts.data.data.repositories.IVersionRepository
import com.pechuro.cashdebts.data.di.component.DaggerDataComponent
import com.pechuro.cashdebts.di.component.AppComponent
import com.pechuro.cashdebts.di.component.DaggerAppComponent
import com.pechuro.cashdebts.ui.utils.BaseEvent
import com.pechuro.cashdebts.ui.utils.EventManager
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject


class App : Application(), HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var serviceDispatchingAndroidInjector: DispatchingAndroidInjector<Service>
    @Inject
    protected lateinit var versionRepository: IVersionRepository

    override fun onCreate() {
        super.onCreate()

        /*   if (LeakCanary.isInAnalyzerProcess(this)) return
           LeakCanary.install(this)*/

        initDI()
        setVersionListener()
    }

    override fun activityInjector() = activityDispatchingAndroidInjector

    override fun serviceInjector() = serviceDispatchingAndroidInjector

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

    @SuppressLint("CheckResult")
    private fun setVersionListener() {
        versionRepository.getCurrentVersion().subscribe({
            if (getDeviceVersion() != it) EventManager.publish(AppEvent.OnNewVersionAvailable, true)
        }, { })
    }

    private fun getDeviceVersion(): Long {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        return PackageInfoCompat.getLongVersionCode(packageInfo)
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}

sealed class AppEvent : BaseEvent() {
    object OnNewVersionAvailable : AppEvent()
}