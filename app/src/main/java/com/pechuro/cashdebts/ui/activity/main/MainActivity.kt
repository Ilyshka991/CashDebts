package com.pechuro.cashdebts.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.ActivityMainBinding
import com.pechuro.cashdebts.ui.base.BaseActivity
import com.pechuro.cashdebts.ui.fragment.debtlist.DebtListFragment
import com.pechuro.cashdebts.ui.utils.transaction

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override val viewModel: MainActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) homeFragment()
    }

    private fun homeFragment() {
        val fragment = DebtListFragment.newInstance()
        supportFragmentManager.transaction {
            replace(viewDataBinding.container.id, fragment)
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
