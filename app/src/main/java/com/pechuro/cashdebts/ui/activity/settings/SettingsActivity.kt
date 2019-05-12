package com.pechuro.cashdebts.ui.activity.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.activity.BaseFragmentActivity
import com.pechuro.cashdebts.ui.fragment.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_scrolling_container.*

class SettingsActivity : BaseFragmentActivity<SettingsActivityViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_scrolling_container
    override val containerId: Int
        get() = container.id

    override fun getViewModelClass() = SettingsActivityViewModel::class

    override fun getHomeFragment() = SettingsFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_action_close)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    companion object {

        fun newIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }
}
