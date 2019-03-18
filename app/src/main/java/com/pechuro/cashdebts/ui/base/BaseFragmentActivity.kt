package com.pechuro.cashdebts.ui.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.base.BaseActivity
import com.pechuro.cashdebts.ui.base.base.BaseFragment
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.transaction

abstract class BaseFragmentActivity<T : ViewDataBinding, VM : BaseViewModel> : BaseActivity<T, VM>() {
    protected abstract val homeFragment: Fragment
    @get:IdRes
    protected abstract val cointainerId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) homeFragment()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            showPreviousFragment()
        } else {
            super.onBackPressed()
        }
    }

    protected fun <T : ViewDataBinding, V : BaseViewModel> showFragment(
        fragment: BaseFragment<T, V>,
        isAddToBackStack: Boolean = true
    ) {
        supportFragmentManager.transaction {
            setCustomAnimations(
                R.anim.anim_fade_in,
                R.anim.anim_fade_out,
                R.anim.anim_fade_in,
                R.anim.anim_fade_out
            )
            replace(cointainerId, fragment)
            if (isAddToBackStack) addToBackStack(null)
        }
    }

    private fun showPreviousFragment() {
        supportFragmentManager.popBackStack()
    }

    fun homeFragment() {
        supportFragmentManager.transaction {
            replace(cointainerId, homeFragment)
        }
    }
}