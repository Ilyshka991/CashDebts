package com.pechuro.cashdebts.ui.fragment.profileedit

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.BR
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.databinding.FragmentProfileEditBinding
import com.pechuro.cashdebts.ui.base.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.picturetakeoptions.AddOptionsEvent
import com.pechuro.cashdebts.ui.fragment.picturetakeoptions.PictureTakeOptionsDialog
import com.pechuro.cashdebts.ui.fragment.progressdialog.ProgressDialog
import com.pechuro.cashdebts.ui.utils.EventBus
import com.pechuro.cashdebts.ui.utils.transaction
import io.reactivex.rxkotlin.addTo

class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding, ProfileEditFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_profile_edit
    override val bindingVariables: Map<Int, Any>
        get() = mapOf(BR.viewModel to viewModel)

    override fun getViewModelClass() = ProfileEditFragmentViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewListeners()
        loadUserIfRequire()
    }

    override fun onStart() {
        super.onStart()
        setEventListeners()
        setViewModelListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK -> loadEditedAvatar()
            requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK -> {
                val uri = data?.data
                val inputStream = uri?.let { context?.contentResolver?.openInputStream(it) }
                inputStream?.let {
                    viewModel.writeToFile(it)
                    loadEditedAvatar()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setViewListeners() {
        with(viewDataBinding) {
            imagePhoto.setOnClickListener {
                showOptionsDialog()
            }
            buttonSave.setOnClickListener {
                this@ProfileEditFragment.viewModel.save()
            }
            fabTakePhoto.setOnClickListener {
                showOptionsDialog()
            }
        }
    }

    private fun setViewModelListener() {
        viewModel.command.subscribe {
            when (it) {
                is ProfileEditFragmentViewModel.Events.OnUserStartLoad -> showProgressDialog()
                is ProfileEditFragmentViewModel.Events.OnUserStopLoad -> dismissProgressDialog()
                is ProfileEditFragmentViewModel.Events.OnSaved -> onSaved()
                is ProfileEditFragmentViewModel.Events.OnSaveError -> showSnackbarErrorSave()
                is ProfileEditFragmentViewModel.Events.OnLoadError -> showSnackbarErrorLoad()
            }
        }.addTo(weakCompositeDisposable)
    }

    private fun setEventListeners() {
        EventBus.listen(AddOptionsEvent::class.java).subscribe {
            when (it) {
                is AddOptionsEvent.TakePictureFromCamera -> dispatchTakePictureFromCameraIntent()
                is AddOptionsEvent.TakePictureFromGallery -> dispatchTakePictureFromGalleryIntent()
            }
        }.addTo(weakCompositeDisposable)
    }

    private fun loadUserIfRequire() {
        val isFirstTime = arguments?.getBoolean(ARG_IS_FIRST_TIME)
        if (isFirstTime == false) viewModel.loadExistingUser()
    }

    private fun dispatchTakePictureFromCameraIntent() {
        context?.let { context ->
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(context.packageManager)?.also {
                    val photoFile = viewModel.createImageFile() ?: return
                    photoFile.also {
                        val photoURI = FileProvider.getUriForFile(
                            context,
                            "com.pechuro.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                    }
                }
            }
        }
    }

    private fun dispatchTakePictureFromGalleryIntent() {
        context?.let { context ->
            Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ).also { pickPictureIntent ->
                pickPictureIntent.resolveActivity(context.packageManager)?.also {
                    startActivityForResult(pickPictureIntent, REQUEST_PICK_PHOTO)
                }
            }
        }
    }

    private fun showOptionsDialog() {
        PictureTakeOptionsDialog.newInstance().show(childFragmentManager, PictureTakeOptionsDialog.TAG)
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

    private fun loadEditedAvatar() {
        viewModel.loadEditedAvatar()
    }

    private fun onSaved() {
        EventBus.publish(ProfileEditEvent.OnSaved)
    }

    private fun showSnackbarErrorLoad() {
        Snackbar.make(viewDataBinding.root, R.string.profile_edit_error_load, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.action_retry) {
                viewModel.loadExistingUser()
            }
            .show()
    }

    private fun showSnackbarErrorSave() {
        Snackbar.make(viewDataBinding.root, R.string.profile_edit_error_load, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.action_retry) {
                viewModel.save()
            }
            .show()
    }

    companion object {
        private const val REQUEST_TAKE_PHOTO = 1213
        private const val REQUEST_PICK_PHOTO = 2453

        private const val ARG_IS_FIRST_TIME = "isFirstTime"

        fun newInstance(isFirstTime: Boolean = false) =
            ProfileEditFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_FIRST_TIME, isFirstTime)
                }
            }
    }
}