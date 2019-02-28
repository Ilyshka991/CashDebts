package com.pechuro.cashdebts.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.ActivityContainerBinding
import com.pechuro.cashdebts.ui.activity.adddebt.AddActivity
import com.pechuro.cashdebts.ui.activity.auth.AuthActivity
import com.pechuro.cashdebts.ui.activity.main.debtlist.DebtListFragment
import com.pechuro.cashdebts.ui.base.BaseActivity
import com.pechuro.cashdebts.ui.utils.EventBus
import com.pechuro.cashdebts.ui.utils.transaction

class MainActivity : BaseActivity<ActivityContainerBinding, MainActivityViewModel>() {

    override val viewModel: MainActivityViewModel
        get() = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
    override val layoutId: Int
        get() = R.layout.activity_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) homeFragment()
    }

    override fun onResume() {
        super.onResume()
        subscribeToEvents()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_action_logout -> {
                logout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        viewModel.logout()
        val intent = AuthActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    private fun homeFragment() {
        val fragment = DebtListFragment.newInstance()
        supportFragmentManager.transaction {
            replace(viewDataBinding.container.id, fragment)
        }
    }

    private fun subscribeToEvents() {
        EventBus.listen(MainActivityEvent::class.java).subscribe {
            when (it) {
                is MainActivityEvent.OpenAddActivity -> openAddActivity()
            }
        }.let(weakCompositeDisposable::add)
    }

    private fun openAddActivity() {
        val intent = AddActivity.newIntent(this)
        startActivity(intent)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
