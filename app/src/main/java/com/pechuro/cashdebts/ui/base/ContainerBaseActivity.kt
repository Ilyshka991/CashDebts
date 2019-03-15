package com.pechuro.cashdebts.ui.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.ActivityContainerBinding
import com.pechuro.cashdebts.ui.base.base.BaseActivity
import com.pechuro.cashdebts.ui.base.base.BaseFragment
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.transaction

abstract class ContainerBaseActivity<VM : BaseViewModel> : BaseActivity<ActivityContainerBinding, VM>() {
    protected abstract val homeFragment: Fragment

    override val layoutId: Int
        get() = R.layout.activity_container

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
            replace(viewDataBinding.container.id, fragment)
            if (isAddToBackStack) addToBackStack(null)
        }
    }

    private fun showPreviousFragment() {
        supportFragmentManager.popBackStack()
    }

    private fun homeFragment() {
        supportFragmentManager.transaction {
            replace(viewDataBinding.container.id, homeFragment)
        }
    }
}
