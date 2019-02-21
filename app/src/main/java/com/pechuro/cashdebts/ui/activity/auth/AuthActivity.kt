package com.pechuro.cashdebts.ui.activity.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.databinding.ActivityAuthBinding
import com.pechuro.cashdebts.ui.activity.main.MainActivity
import com.pechuro.cashdebts.ui.base.BaseActivity

class AuthActivity : BaseActivity<ActivityAuthBinding, AuthActivityViewModel>() {
    override val viewModel: AuthActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(AuthActivityViewModel::class.java)
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)
    override val layoutId: Int
        get() = com.pechuro.cashdebts.R.layout.activity_auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToEvents()
        setupView()
    }

    override fun onSupportNavigateUp(): Boolean {
        flipPage(false)
        viewDataBinding.containerAuthPhone.textPhone.selectAll()
        viewDataBinding.containerAuthCode.textCode.text = null
        return true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(BUNDLE_PAGE_INDEX, viewDataBinding.flipper.displayedChild)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val index = savedInstanceState?.getInt(BUNDLE_PAGE_INDEX)
        if (index == 1) flipPage(true)
    }

    private fun setupView() {
        with(viewDataBinding.containerAuthPhone) {
            textPhone.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    buttonSendCode.performClick()
                    return@setOnEditorActionListener true
                }
                false
            }
        }
    }

    private fun subscribeToEvents() {
        viewModel.command.observe(this, Observer {
            when (it) {
                is Events.OnStartVerification -> flipPage(true)
                is Events.OnSuccess -> openNextActivity()
                is Events.ShowSnackBarError -> showSnackBar(it.id)
            }
        })
    }

    private fun showSnackBar(@StringRes id: Int) {
        Snackbar.make(viewDataBinding.root, id, Snackbar.LENGTH_LONG).show()
    }

    private fun openNextActivity() {
        val intent = MainActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    private fun flipPage(isNext: Boolean) {
        with(viewDataBinding.flipper) {
            if (isNext) showNext() else showPrevious()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(isNext)
    }

    companion object {
        private const val BUNDLE_PAGE_INDEX = "isNextPage"

        fun newIntent(context: Context) = Intent(context, AuthActivity::class.java)
    }
}
