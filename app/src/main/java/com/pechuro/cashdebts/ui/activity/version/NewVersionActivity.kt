package com.pechuro.cashdebts.ui.activity.version

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.activity.BaseFragmentActivity
import com.pechuro.cashdebts.ui.fragment.version.NewVersionFragment
import kotlinx.android.synthetic.main.activity_container.*

class NewVersionActivity : BaseFragmentActivity<NewVersionActivityViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_container
    override val containerId: Int
        get() = container.id

    override fun getViewModelClass() = NewVersionActivityViewModel::class

    override fun getHomeFragment() = NewVersionFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
    }

    companion object {

        fun newIntent(context: Context) = Intent(context, NewVersionActivity::class.java)
    }
}
