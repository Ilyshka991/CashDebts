package com.pechuro.cashdebts.ui.base

import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.base.BaseFragment
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.transaction
import kotlinx.android.synthetic.main.activity_container.*

abstract class FragmentSwitcherBaseActivity<VM : BaseViewModel> : BaseFragmentActivity<VM>() {
    override val layoutId: Int
        get() = R.layout.activity_container
    override val containerId: Int
        get() = container.id

    protected abstract val isCloseButtonEnabled: Boolean

    protected var isBackAllowed = true

    protected val backStackSize
        get() = supportFragmentManager.backStackEntryCount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isBackAllowed = savedInstanceState?.getBoolean(BUNDLE_IS_BACK_ALLOWED) ?: isBackAllowed
        setBackStackListener()
        setupActionBar(supportFragmentManager.backStackEntryCount)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(BUNDLE_IS_BACK_ALLOWED, isBackAllowed)
    }

    final override fun <V : BaseViewModel> showFragment(
        fragment: BaseFragment<V>,
        isAddToBackStack: Boolean
    ) {
        supportFragmentManager.transaction {
            setCustomAnimations(
                R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left,
                R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right
            )
            replace(containerId, fragment)
            if (isAddToBackStack) addToBackStack(null)
        }
    }

    private fun setBackStackListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            setupActionBar(supportFragmentManager.backStackEntryCount)
        }
    }

    private fun setupActionBar(backStackCount: Int = 0) {
        if (!isBackAllowed) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            return
        }

        if (isCloseButtonEnabled && backStackCount == 0) {
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_close_white)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            return
        }
        if (isCloseButtonEnabled) supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_back_white)
        supportActionBar?.setDisplayHomeAsUpEnabled(backStackCount != 0)
    }

    companion object {
        private const val BUNDLE_IS_BACK_ALLOWED = "isBackEnabled"
    }
}
