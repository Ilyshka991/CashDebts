package com.pechuro.cashdebts.ui.activity.base

import android.os.Bundle
import android.view.View.*
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.ActivitySwitcherContainerBinding
import com.pechuro.cashdebts.ui.base.BaseActivity
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.transaction

abstract class FragmentSwitcherBaseActivity<VM : BaseViewModel> : BaseActivity<ActivitySwitcherContainerBinding, VM>() {
    protected abstract val isCloseButtonEnabled: Boolean
    protected abstract val homeFragment: Fragment
    protected open val label: Int? = null

    override val layoutId: Int
        get() = R.layout.activity_switcher_container

    protected abstract fun onDoneButtonClick(currentPosition: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActionBar()
        setListeners()
        setupActionBar()
        if (savedInstanceState == null) homeFragment()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            showPreviousFragment()
        } else {
            super.onBackPressed()
        }
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

    private fun homeFragment() {
        supportFragmentManager.transaction {
            replace(viewDataBinding.container.id, homeFragment)
        }
    }

    private fun showPreviousFragment() {
        supportFragmentManager.popBackStack()

    }

    private fun setListeners() {
        with(viewDataBinding) {
            buttonBack.setOnClickListener {
                showPreviousFragment()
            }
            buttonDone.setOnClickListener {
                onDoneButtonClick(supportFragmentManager.backStackEntryCount)
            }
            buttonClose.setOnClickListener {
                finish()
            }
        }
        supportFragmentManager.addOnBackStackChangedListener {
            setupActionBar()
        }
    }

    private fun setupActionBar() {
        with(viewDataBinding) {
            when {
                supportFragmentManager.backStackEntryCount > 0 -> {
                    buttonClose.visibility = GONE
                    buttonBack.visibility = VISIBLE
                }
                isCloseButtonEnabled -> {
                    buttonClose.visibility = VISIBLE
                    buttonBack.visibility = INVISIBLE
                }
                else -> {
                    buttonClose.visibility = GONE
                    buttonBack.visibility = GONE
                }
            }
        }
    }

    private fun initActionBar() {
        label?.let { viewDataBinding.textLabel.setText(it) }
        setSupportActionBar(viewDataBinding.toolbar)
    }
}
