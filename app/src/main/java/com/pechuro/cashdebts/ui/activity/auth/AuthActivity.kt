package com.pechuro.cashdebts.ui.activity.auth

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.ActivityAuthBinding
import com.pechuro.cashdebts.ui.base.BaseActivity

class AuthActivity : BaseActivity<ActivityAuthBinding, AuthActivityViewModel>() {

    override val viewModel: AuthActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(AuthActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_auth


    companion object {

        fun newIntent(context: Context) = Intent(context, AuthActivity::class.java)
    }
}
