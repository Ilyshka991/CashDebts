package com.pechuro.cashdebts.ui.fragment.profileedit

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.pechuro.cashdebts.data.repositories.FirebaseStorageRepository
import com.pechuro.cashdebts.data.repositories.FirestoreUserRepository
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.profileedit.model.ProfileEditModel
import com.pechuro.cashdebts.ui.utils.BaseEvent
import com.pechuro.cashdebts.utils.AVATAR_PATH
import com.pechuro.cashdebts.utils.isExternalStorageWritable
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ProfileEditFragmentViewModel @Inject constructor(
    private val userRepository: FirestoreUserRepository,
    private val storageRepository: FirebaseStorageRepository,
    /*private val currentUser: CurrentUser,*/
    private val appContext: Context
) : BaseViewModel() {

    val data = ProfileEditModel()
    val localAvatarUri = ObservableField<Uri?>()
    val isLoading = ObservableBoolean()

    private var localAvatarFile: File? = null

    private var isUserAlreadyLoaded = false

    fun getExistingUser() {
        /* if (isUserAlreadyLoaded) return
         EventBus.publish(ProfileEditEvent.OnUserStartLoad)
         isUserAlreadyLoaded = true
         userRepository.get(currentUser.uid!!)
             .subscribe({
                 data.setUser(it)
                 EventBus.publish(ProfileEditEvent.OnUserStopLoad)
             }, {
                 EventBus.publish(ProfileEditEvent.OnUserStopLoad)
                 it.printStackTrace()
             }).addTo(compositeDisposable)*/
    }

    fun save() {
        /* if (!data.isValid()) return
         isLoading.set(true)
         val task = if (localAvatarFile != null) {
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
         } else {
             val user = FirestoreUser(
                 data.fields.firstName,
                 data.fields.lastName,
                 currentUser.phoneNumber!!,
                 data.fields.imageUrl.toString()
             )
             userRepository.setUser(currentUser.uid!!, user)
         }
         task.subscribe({
             isLoading.set(false)
         }, {
             it.printStackTrace()
         }).addTo(compositeDisposable)*/
    }

    fun loadEditedAvatar() {
        println(localAvatarFile?.toUri())
        localAvatarUri.set(localAvatarFile?.toUri())
    }

    fun createImageFile() = try {
        /* createAvatarFile(appContext, "${currentUser.uid!!}.jpg")?.also {
             localAvatarFile = it
         }*/
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

sealed class ProfileEditEvent : BaseEvent() {
    object OnUserStopLoad : ProfileEditEvent()
    object OnUserStartLoad : ProfileEditEvent()
}