package com.pechuro.cashdebts.ui.fragment.profileview

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditEvent
import com.pechuro.cashdebts.ui.fragment.progressdialog.ProgressDialog
import com.pechuro.cashdebts.ui.utils.EventBus
import com.pechuro.cashdebts.ui.utils.transaction
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_profile_view.*

class ProfileViewFragment : BaseFragment<ProfileViewFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_profile_view

    override fun getViewModelClass() = ProfileViewFragmentViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setListeners()
        setEventListener()
    }

    override fun onStart() {
        super.onStart()
        setViewModelListener()
    }

    private fun setListeners() {
        button_edit.setOnClickListener {
            EventBus.publish(ProfileViewEvent.OpenEditProfile)
        }
        button_logout.setOnClickListener {
            EventBus.publish(ProfileViewEvent.OnLogout)
        }
    }

    private fun setViewModelListener() {
        viewModel.loadingState.subscribe {
            when (it) {
                is ProfileViewFragmentViewModel.LoadingState.OnStart -> showProgressDialog()
                is ProfileViewFragmentViewModel.LoadingState.OnStop -> dismissProgressDialog()
                is ProfileViewFragmentViewModel.LoadingState.OnError -> showErrorSnackbar()
            }
        }.addTo(weakCompositeDisposable)
    }

    private fun setEventListener() {
        EventBus.listen(ProfileEditEvent::class.java).subscribe {
            when (it) {
                is ProfileEditEvent.OnSaved -> viewModel.loadUser()
            }
        }.addTo(strongCompositeDisposable)
    }

    private fun showProgressDialog() {
        childFragmentManager.transaction {
            add(ProgressDialog.newInstance(), ProgressDialog.TAG)
            addToBackStack(ProgressDialog.TAG)
        }
    }

    private fun dismissProgressDialog() {
        childFragmentManager.popBackStack()
    }

    private fun showErrorSnackbar() {
        Snackbar.make(layout_coordinator, R.string.error_load, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.action_retry) {
                viewModel.loadUser()
            }
            .show()
    }

    companion object {
        fun newInstance() = ProfileViewFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}

