package com.pechuro.cashdebts.ui.base.activity

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.transaction

abstract class BaseFragmentActivity<VM : BaseViewModel> : BaseActivity<VM>() {
    @get:IdRes
    protected abstract val containerId: Int

    protected abstract fun getHomeFragment(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) homeFragment()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            showPreviousFragment()
        } else {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    protected fun homeFragment() {
        supportFragmentManager.transaction {
            replace(containerId, getHomeFragment())
        }
    }

    protected open fun <V : BaseViewModel> showFragment(
        fragment: BaseFragment<V>,
        isAddToBackStack: Boolean = true
    ) {
        supportFragmentManager.transaction {
            setCustomAnimations(
                R.anim.anim_fade_in,
                R.anim.anim_fade_out,
                R.anim.anim_fade_in,
                R.anim.anim_fade_out
            )
            replace(containerId, fragment)
            if (isAddToBackStack) addToBackStack(null)
        }
    }

    protected fun showPreviousFragment() {
        supportFragmentManager.popBackStack()
    }
}