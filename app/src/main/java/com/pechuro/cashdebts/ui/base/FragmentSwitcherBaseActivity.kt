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

abstract class FragmentSwitcherBaseActivity<VM : BaseViewModel> : BaseActivity<ActivityContainerBinding, VM>() {
    protected abstract val isCloseButtonEnabled: Boolean
    protected abstract val homeFragment: Fragment

    override val layoutId: Int
        get() = R.layout.activity_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) homeFragment()
        setBackStackListener()
        setupActionBar(supportFragmentManager.backStackEntryCount)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            showPreviousFragment()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    protected fun <T : ViewDataBinding, V : BaseViewModel> showNextFragment(fragment: BaseFragment<T, V>) {
        supportFragmentManager.transaction {
            setCustomAnimations(
                R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left,
                R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right
            )
            replace(viewDataBinding.container.id, fragment)
            addToBackStack(null)
        }
    }

    private fun showPreviousFragment() {
        supportFragmentManager.popBackStack()
    }

    private fun setBackStackListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            setupActionBar(supportFragmentManager.backStackEntryCount)
        }
    }

    private fun setupActionBar(backStackCount: Int = 0) {
        if (isCloseButtonEnabled && backStackCount == 0) {
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_close_white)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            return
        }
        if (isCloseButtonEnabled) supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_back_white)
        supportActionBar?.setDisplayHomeAsUpEnabled(backStackCount != 0)
    }

    private fun homeFragment() {
        supportFragmentManager.transaction {
            replace(viewDataBinding.container.id, homeFragment)
        }
    }
}
