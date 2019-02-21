package com.pechuro.cashdebts.ui.activity.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.ActivityAuthBinding
import com.pechuro.cashdebts.ui.activity.main.MainActivity
import com.pechuro.cashdebts.ui.base.BaseActivity

class AuthActivity : BaseActivity<ActivityAuthBinding, AuthActivityViewModel>() {
    override val viewModel: AuthActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(AuthActivityViewModel::class.java)
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)
    override val layoutId: Int
        get() = R.layout.activity_auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        viewModel.command.observe(this, Observer {
            when (it) {
                is Events.OnStartVerification -> viewDataBinding.flipper.showNext()
                is Events.OnSuccess -> {
                    val intent = MainActivity.newIntent(this)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, AuthActivity::class.java)
    }
}
