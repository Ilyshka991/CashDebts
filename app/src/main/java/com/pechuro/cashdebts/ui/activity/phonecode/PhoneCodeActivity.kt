package com.pechuro.cashdebts.ui.activity.phonecode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.ActivityPhoneCodeBinding
import com.pechuro.cashdebts.ui.base.BaseActivity

class PhoneCodeActivity : BaseActivity<ActivityPhoneCodeBinding, PhoneCodeActivityViewModel>() {

    override val viewModel: PhoneCodeActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(PhoneCodeActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_phone_code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActionBar()
    }

    private fun initActionBar() {
        setSupportActionBar(viewDataBinding.toolbar)
    }


    companion object {
        fun newIntent(context: Context) = Intent(context, PhoneCodeActivity::class.java)
    }
}
