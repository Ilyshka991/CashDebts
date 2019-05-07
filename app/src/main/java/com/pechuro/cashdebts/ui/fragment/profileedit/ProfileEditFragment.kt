package com.pechuro.cashdebts.ui.fragment.profileedit

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseFragment
import com.pechuro.cashdebts.ui.fragment.picturetakeoptions.AddOptionsEvent
import com.pechuro.cashdebts.ui.fragment.picturetakeoptions.PictureTakeOptionsDialog
import com.pechuro.cashdebts.ui.fragment.profileedit.ProfileEditFragmentViewModel.Events.*
import com.pechuro.cashdebts.ui.utils.EventManager
import com.pechuro.cashdebts.ui.utils.binding.receiveTextChangesFrom
import com.pechuro.cashdebts.ui.utils.extensions.loadAvatar
import com.pechuro.cashdebts.ui.utils.extensions.setError
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_profile_edit.*

class ProfileEditFragment : BaseFragment<ProfileEditFragmentViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_profile_edit

    override fun getViewModelClass() = ProfileEditFragmentViewModel::class

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setViewListeners()
        loadUserIfRequire()
    }

    override fun onStart() {
        super.onStart()
        setEventListeners()
        setViewModelListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK -> loadEditedAvatar()
            requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK -> data?.data?.let { onPhotoPick(it) }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setViewListeners() {
        image_photo.setOnClickListener {
            showOptionsDialog()
        }
        button_save.setOnClickListener {
            viewModel.save()
        }
        fab_take_photo.setOnClickListener {
            showOptionsDialog()
        }
    }

    private fun setViewModelListeners() {
        with(viewModel) {
            weakCompositeDisposable.addAll(
                command.subscribe {
                    when (it) {
                        is OnUserStartLoad -> showProgressDialog()
                        is OnUserStopLoad -> dismissProgressDialog()
                        is OnSaved -> onSaved()
                        is OnSaveError -> showSnackbarErrorSave()
                        is OnLoadError -> showSnackbarErrorLoad()
                    }
                },
                loadingState.subscribe {
                    setLoading(it)
                },
                isConnectionAvailable.subscribe {
                    onConnectionChanged(it)
                },
                imageUrl.subscribe {
                    image_photo.loadAvatar(it)
                }
            )

            with(inputData) {
                with(fields) {
                    firstName.receiveTextChangesFrom(text_first_name).addTo(weakCompositeDisposable)
                    lastName.receiveTextChangesFrom(text_last_name).addTo(weakCompositeDisposable)
                }
                with(errors) {
                    firstNameError.subscribe {
                        text_first_name_layout.setError(it)
                    }.addTo(weakCompositeDisposable)
                    lastNameError.subscribe {
                        text_last_name_layout.setError(it)
                    }.addTo(weakCompositeDisposable)
                }
            }
        }
    }

    private fun setEventListeners() {
        EventManager.listen(AddOptionsEvent::class.java).subscribe {
            when (it) {
                is AddOptionsEvent.TakePictureFromCamera -> dispatchTakePictureFromCameraIntent()
                is AddOptionsEvent.TakePictureFromGallery -> dispatchTakePictureFromGalleryIntent()
            }
        }.addTo(weakCompositeDisposable)
    }

    private fun loadUserIfRequire() {
        val isFirstTime = arguments?.getBoolean(ARG_IS_FIRST_TIME) ?: false
        viewModel.loadUser(!isFirstTime)
    }

    private fun onConnectionChanged(isAvailable: Boolean) {
        button_save.isEnabled = isAvailable
        view_no_connection.isVisible = !isAvailable
    }

    private fun setLoading(isLoading: Boolean) {
        button_save.setProgress(isLoading)
        image_photo.isEnabled = !isLoading
        fab_take_photo.isEnabled = !isLoading
    }

    private fun dispatchTakePictureFromCameraIntent() {
        context?.let { context ->
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(context.packageManager)?.also {
                    val photoFile = viewModel.createImageFile() ?: return
                    photoFile.also {
                        val photoURI = FileProvider.getUriForFile(
                            context,
                            "com.pechuro.cashdebts.fileprovider",
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

    private fun onPhotoPick(uri: Uri) {
        val inputStream = uri.let { context?.contentResolver?.openInputStream(it) }
        inputStream?.let {
            viewModel.writeToFile(it)
            loadEditedAvatar()
        }
    }

    private fun showOptionsDialog() {
        PictureTakeOptionsDialog.newInstance().show(childFragmentManager, PictureTakeOptionsDialog.TAG)
    }

    private fun loadEditedAvatar() {
        viewModel.loadEditedAvatar()
    }

    private fun onSaved() {
        EventManager.publish(ProfileEditEvent.OnSaved)
    }

    private fun showSnackbarErrorLoad() {
        Snackbar.make(container, R.string.profile_edit_error_load, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.action_retry) {
                viewModel.loadUser()
            }
            .show()
    }

    private fun showSnackbarErrorSave() {
        Snackbar.make(container, R.string.profile_edit_error_load, Snackbar.LENGTH_INDEFINITE)
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