package com.pechuro.cashdebts.ui.fragment.profileview

import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.data.model.FirestoreUser
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.activity.main.SnackActionInfo
import com.pechuro.cashdebts.ui.activity.main.SnackInfo
import com.pechuro.cashdebts.ui.activity.main.SnackbarManager
import com.pechuro.cashdebts.ui.utils.loadAvatar
import kotlinx.android.synthetic.main.fragment_profile_view.*

class ProfileViewFragment : BaseFragment<ProfileViewFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_profile_view

    override fun getViewModelClass() = ProfileViewFragmentViewModel::class

    override fun onStart() {
        super.onStart()
        setViewModelListeners()
    }

    private fun setViewModelListeners() {
        with(viewModel) {
            weakCompositeDisposable.addAll(
                loadingState.subscribe {
                    when (it) {
                        is ProfileViewFragmentViewModel.LoadingState.OnStart -> {
                            container_auth_phone.isVisible = false
                            progress.isVisible = true
                        }
                        is ProfileViewFragmentViewModel.LoadingState.OnStop -> {
                            container_auth_phone.isVisible = true
                            progress.isVisible = false
                        }
                        is ProfileViewFragmentViewModel.LoadingState.OnError -> showErrorSnackbar()
                    }
                },
                user.subscribe {
                    setUser(it)
                }
            )
        }
    }

    private fun setUser(user: FirestoreUser) {
        with(user) {
            image_avatar.loadAvatar(photoUrl)
            text_first_name.text = firstName
            text_last_name.text = lastName
            text_phone.text = phoneNumber
        }
    }

    private fun showErrorSnackbar() {
        SnackbarManager.show(
            SnackInfo(
                R.string.error_load,
                Snackbar.LENGTH_INDEFINITE,
                SnackActionInfo(
                    R.string.action_retry,
                    viewModel::loadUser
                )
            )
        )
    }

    companion object {
        fun newInstance() = ProfileViewFragment()
    }
}

