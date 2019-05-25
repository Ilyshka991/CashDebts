package com.pechuro.cashdebts

/*DO not touch this
* if removed:
* com.pechuro.cashdebts.calculator.di.component.DaggerCalculatorComponent
* */
import com.pechuro.cashdebts.calculator.di.component.DaggerCalculatorComponent
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import android.content.res.Configuration
import androidx.core.content.pm.PackageInfoCompat
import com.pechuro.cashdebts.data.data.repositories.IVersionRepository
import com.pechuro.cashdebts.data.di.component.DaggerDataComponent
import com.pechuro.cashdebts.di.component.DaggerAppComponent
import com.pechuro.cashdebts.model.locale.LocaleManager
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.model.theme.AppTheme
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
    @Inject
    protected lateinit var prefsManager: PrefsManager

    override fun onCreate() {
        super.onCreate()
        initDI()
        AppTheme.setTheme(prefsManager.settingTheme)
        setVersionListener()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.updateLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        LocaleManager.updateLocale(this)
    }

    override fun activityInjector() = activityDispatchingAndroidInjector

    override fun serviceInjector() = serviceDispatchingAndroidInjector

    private fun initDI() {
        val dataComponent = DaggerDataComponent.create()
        val calculatorComponent = DaggerCalculatorComponent.create()
        DaggerAppComponent.builder()
            .application(this)
            .dataComponent(dataComponent)
            .calculatorComponent(calculatorComponent)
            .build().run {
                inject(this@App)
            }
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
}

sealed class AppEvent : BaseEvent() {
    object OnNewVersionAvailable : AppEvent()
}