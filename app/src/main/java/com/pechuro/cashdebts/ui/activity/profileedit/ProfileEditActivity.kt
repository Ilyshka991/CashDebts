package com.pechuro.cashdebts.ui.activity.profileedit

import android.content.Context
import android.content.Intent
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseFragmentActivity
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditEvent
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditFragment
import com.pechuro.cashdebts.ui.utils.EventBus
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_container.*

class ProfileEditActivity : BaseFragmentActivity<ProfileEditActivityViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_container
    override val containerId: Int
        get() = container.id

    override fun getViewModelClass() = ProfileEditActivityViewModel::class

    override fun getHomeFragment() = ProfileEditFragment.newInstance()

    override fun onStart() {
        super.onStart()
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        EventBus.listen(ProfileEditEvent::class.java).subscribe {
            when (it) {
                is ProfileEditEvent.OnSaved -> finish()
            }
        }.addTo(weakCompositeDisposable)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, ProfileEditActivity::class.java)
    }
}