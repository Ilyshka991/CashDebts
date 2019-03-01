package com.pechuro.cashdebts.ui.activity.base

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.fragment.app.FragmentTransaction
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.ActivitySwitcherContainerBinding
import com.pechuro.cashdebts.ui.base.BaseActivity
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.transaction

abstract class FragmentSwitcherBaseActivity<VM : BaseViewModel> : BaseActivity<ActivitySwitcherContainerBinding, VM>() {
    override val layoutId: Int
        get() = R.layout.activity_switcher_container
    abstract val isCloseButtonEnabled: Boolean

    protected abstract fun homeFragment()

    protected abstract fun onDoneButtonClick()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActionBar()
        setListeners()
        setupActionBar()
        if (savedInstanceState == null) homeFragment()
    }

    override fun onSupportNavigateUp(): Boolean {
        showPreviousFragment()
        setupActionBar()
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            showPreviousFragment()
            setupActionBar()
        } else {
            super.onBackPressed()
        }
    }

    protected fun showNextFragment(body: FragmentTransaction.() -> Unit) {
        supportFragmentManager.transaction {
            setCustomAnimations(
                R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left,
                R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right
            )
            body()
            addToBackStack(null)
        }
    }

    protected fun showPreviousFragment() {
        supportFragmentManager.popBackStack()
    }

    private fun setListeners() {
        with(viewDataBinding) {
            buttonBack.setOnClickListener {
                showPreviousFragment()
                setupActionBar()
            }
            buttonDone.setOnClickListener {
                onDoneButtonClick()
                setupActionBar()
            }
            buttonClose.setOnClickListener {

            }
        }
    }

    private fun setupActionBar() {
        with(viewDataBinding) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                if (isCloseButtonEnabled) {
                    buttonClose.visibility = INVISIBLE
                }
                buttonBack.visibility = VISIBLE
            } else {
                if (isCloseButtonEnabled) {
                    buttonClose.visibility = VISIBLE
                }
                buttonBack.visibility = INVISIBLE
            }
        }
    }

    private fun initActionBar() {
        setSupportActionBar(viewDataBinding.toolbar)
    }

}
