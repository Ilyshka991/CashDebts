package com.pechuro.cashdebts.ui.base.activity

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.extensions.transaction

abstract class BaseFragmentActivity<VM : BaseViewModel> : BaseActivity<VM>() {
    @get:IdRes
    protected abstract val containerId: Int

    private var pendingFragmentTransaction: PendingFragmentTransaction? = null

    protected abstract fun getHomeFragment(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) homeFragment()
    }

    override fun onPostResume() {
        super.onPostResume()
        pendingFragmentTransaction?.let {
            showFragment(it.fragment, it.isAddToBackStack)
            pendingFragmentTransaction = null
        }
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

    protected open fun homeFragment() {
        supportFragmentManager.transaction {
            replace(containerId, getHomeFragment())
        }
    }

    protected open fun <V : BaseViewModel> showFragment(
        fragment: BaseFragment<V>,
        isAddToBackStack: Boolean = true
    ) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
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
        } else {
            pendingFragmentTransaction = PendingFragmentTransaction(fragment, isAddToBackStack)
        }
    }

    protected fun showPreviousFragment() {
        supportFragmentManager.popBackStack()
    }
}

private class PendingFragmentTransaction(
    val fragment: BaseFragment<*>,
    val isAddToBackStack: Boolean
)