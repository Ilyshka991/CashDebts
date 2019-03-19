package com.pechuro.cashdebts.ui.fragment.profileview

import android.os.Bundle
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentProfileViewBinding
import com.pechuro.cashdebts.ui.base.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditEvent
import com.pechuro.cashdebts.ui.fragment.progressdialog.ProgressDialog
import com.pechuro.cashdebts.ui.utils.EventBus
import com.pechuro.cashdebts.ui.utils.transaction
import io.reactivex.rxkotlin.addTo

class ProfileViewFragment : BaseFragment<FragmentProfileViewBinding, ProfileViewFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_profile_view
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)

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
        with(viewDataBinding) {
            buttonEdit.setOnClickListener {
                EventBus.publish(ProfileViewEvent.OpenEditProfile)
            }
            buttonLogout.setOnClickListener {
                EventBus.publish(ProfileViewEvent.OnLogout)
            }
        }
    }

    private fun setViewModelListener() {
        viewModel.loadingState.subscribe {
            when (it) {
                is ProfileViewFragmentViewModel.LoadingState.OnStart -> showProgressDialog()
                is ProfileViewFragmentViewModel.LoadingState.OnStop -> dismissProgressDialog()
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

    companion object {
        fun newInstance() = ProfileViewFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}

