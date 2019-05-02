package com.pechuro.cashdebts.ui.activity.countryselection

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.base.activity.BaseFragmentActivity
import com.pechuro.cashdebts.ui.fragment.countyselection.CountrySelectionFragment
import com.pechuro.cashdebts.ui.utils.BaseEvent
import com.pechuro.cashdebts.ui.utils.EventBus
import kotlinx.android.synthetic.main.activity_container.*

class CountrySelectionActivity : BaseFragmentActivity<CountrySelectionActivityViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_container
    override val containerId: Int
        get() = container.id

    override fun getViewModelClass() = CountrySelectionActivityViewModel::class

    override fun getHomeFragment() = CountrySelectionFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()
    }

    override fun onStart() {
        super.onStart()
        subscribeToEvents()
    }

    override fun onSupportNavigateUp(): Boolean {
        onCanceled()
        return true
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_action_close)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun subscribeToEvents() {
        EventBus.listen(CountySelectEvent::class.java).subscribe {
            when (it) {
                is CountySelectEvent.OnCountrySelect -> onCountrySelected(it.country)
            }
        }.let(weakCompositeDisposable::add)
    }

    private fun onCanceled() {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    private fun onCountrySelected(country: CountryData) {
        val intent = Intent().apply {
            putExtra(INTENT_DATA_SELECTED_COUNTRY, country)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        const val INTENT_DATA_SELECTED_COUNTRY = "country"

        fun newIntent(context: Context) = Intent(context, CountrySelectionActivity::class.java)
    }
}

sealed class CountySelectEvent : BaseEvent() {
    class OnCountrySelect(val country: CountryData) : CountySelectEvent()
}