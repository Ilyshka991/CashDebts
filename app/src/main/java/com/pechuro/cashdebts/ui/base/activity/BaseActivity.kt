package com.pechuro.cashdebts.ui.base.activity

import android.content.Context
import android.content.pm.PackageManager.GET_META_DATA
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.AppEvent
import com.pechuro.cashdebts.model.locale.LocaleManager
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.ui.activity.version.NewVersionActivity
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.EventManager
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import kotlin.reflect.KClass


abstract class BaseActivity<V : BaseViewModel> : AppCompatActivity(),
    HasSupportFragmentInjector {
    @Inject
    protected lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory
    protected lateinit var viewModel: V

    @get:LayoutRes
    protected abstract val layoutId: Int

    @Inject
    protected lateinit var weakCompositeDisposable: CompositeDisposable
    @Inject
    protected lateinit var strongCompositeDisposable: CompositeDisposable
    @Inject
    protected lateinit var prefsManager: PrefsManager

    protected abstract fun getViewModelClass(): KClass<V>

    override fun onCreate(savedInstanceState: Bundle?) {
        performDI()
        initViewModel()
        super.onCreate(savedInstanceState)
        setTheme(prefsManager.settingTheme)
        setContentView(layoutId)
        setAppEventListener()
        resetTitle()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.updateLocale(base))
    }

    override fun onStop() {
        super.onStop()
        weakCompositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        strongCompositeDisposable.clear()
    }

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    private fun performDI() = AndroidInjection.inject(this)

    private fun setTheme(theme: String) {
        val mode = when (theme) {
            "auto" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            "dark" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> throw IllegalArgumentException("Unknown theme")
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass().java)
    }

    private fun resetTitle() {
        val info = packageManager.getActivityInfo(componentName, GET_META_DATA)
        if (info.labelRes != 0) {
            setTitle(info.labelRes)
        }
    }

    private fun setAppEventListener() {
        EventManager.listen(AppEvent::class.java, true).subscribe {
            when (it) {
                is AppEvent.OnNewVersionAvailable -> openNewVersionActivity()
            }
        }.addTo(strongCompositeDisposable)
    }

    private fun openNewVersionActivity() {
        if (this !is NewVersionActivity) {
            val intent = NewVersionActivity.newIntent(this)
            startActivity(intent)
            finish()
        }
    }
}