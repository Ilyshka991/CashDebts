package com.pechuro.cashdebts.ui.activity.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.activity.BaseFragmentActivity
import com.pechuro.cashdebts.ui.fragment.settings.SettingsFragment
import com.pechuro.cashdebts.ui.fragment.settings.SettingsFragmentEvent
import com.pechuro.cashdebts.ui.utils.EventManager
import io.reactivex.rxkotlin.addTo
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
        setEventListeners()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_action_close)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setEventListeners() {
        EventManager.listen(SettingsFragmentEvent::class.java).subscribe {
            when (it) {
                is SettingsFragmentEvent.OnLanguageChanged -> restart()
            }
        }.addTo(strongCompositeDisposable)
    }

    private fun restart() {
        val intent = intent.apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        overridePendingTransition(0, 0)
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
    }

    companion object {

        fun newIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }
}
