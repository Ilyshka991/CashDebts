package com.pechuro.cashdebts.ui.activity.profileedit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.activity.BaseFragmentActivity
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditEvent
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditFragment
import com.pechuro.cashdebts.ui.utils.EventManager
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_scrolling_container.*

class ProfileEditActivity : BaseFragmentActivity<ProfileEditActivityViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_scrolling_container
    override val containerId: Int
        get() = container.id

    override fun getViewModelClass() = ProfileEditActivityViewModel::class

    override fun getHomeFragment() = ProfileEditFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        EventManager.listen(ProfileEditEvent::class.java).subscribe {
            when (it) {
                is ProfileEditEvent.OnSaved -> finish()
            }
        }.addTo(weakCompositeDisposable)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, ProfileEditActivity::class.java)
    }
}