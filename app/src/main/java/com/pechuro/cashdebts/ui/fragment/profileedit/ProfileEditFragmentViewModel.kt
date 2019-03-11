package com.pechuro.cashdebts.ui.fragment.profileedit

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.databinding.ObservableField
import com.google.firebase.auth.FirebaseAuth
import com.pechuro.cashdebts.data.CurrentUser
import com.pechuro.cashdebts.data.FirebaseStorageRepository
import com.pechuro.cashdebts.data.FirestoreUserRepository
import com.pechuro.cashdebts.data.model.FirestoreUser
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.profileedit.model.ProfileEditModel
import com.pechuro.cashdebts.utils.AVATAR_PATH
import com.pechuro.cashdebts.utils.isExternalStorageWritable
import io.reactivex.rxkotlin.addTo
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ProfileEditFragmentViewModel @Inject constructor(
    private val userRepository: FirestoreUserRepository,
    private val auth: FirebaseAuth,
    private val storageRepository: FirebaseStorageRepository,
    private val currentUser: CurrentUser,
    private val appContext: Context
) : BaseViewModel() {

    init {
        getExistingUser()
    }

    val data = ProfileEditModel()
    val localAvatarUri = ObservableField<Uri?>()

    private var localAvatarFile: File? = null

    fun getExistingUser() {
        userRepository.get(auth.currentUser?.uid!!)
            .subscribe({
                data.setUser(it)
            }, {

            }).addTo(compositeDisposable)
    }

    //TODO!!!!!!!!!!!
    fun save() {
        if (!data.isValid()) return
        if (localAvatarFile != null) {
            storageRepository.uploadAndGetUrl(localAvatarFile!!.toUri(), localAvatarFile!!.name)
                .flatMapCompletable {
                    val user = FirestoreUser(
                        data.fields.firstName,
                        data.fields.lastName,
                        currentUser.phoneNumber!!,
                        it.toString()
                    )
                    userRepository.setUser(currentUser.uid!!, user)
                }
                .subscribe {
                    println("COMPLEEEEEEEEEEEEEEEEEEEEETE")
                }.addTo(compositeDisposable)

        }
    }

    fun loadEditedAvatar() {
        localAvatarUri.set(localAvatarFile?.toUri())
    }

    fun createImageFile() = try {
        createAvatarFile(appContext, "${currentUser.uid!!}.jpg")?.also {
            localAvatarFile = it
        }
    } catch (ex: IOException) {
        null
    }

    @Throws(IOException::class)
    private fun createAvatarFile(context: Context?, name: String): File? {
        if (!isExternalStorageWritable()) {
            return null
        }
        val storageDir = context?.let { File(it.filesDir, AVATAR_PATH) } ?: return null
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        return File(storageDir, name)
    }
}