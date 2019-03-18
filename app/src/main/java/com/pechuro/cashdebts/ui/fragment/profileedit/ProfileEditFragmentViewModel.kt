package com.pechuro.cashdebts.ui.fragment.profileedit

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.pechuro.cashdebts.data.model.FirestoreUser
import com.pechuro.cashdebts.data.repositories.IStorageRepository
import com.pechuro.cashdebts.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.profileedit.model.ProfileEditModel
import com.pechuro.cashdebts.ui.utils.BaseEvent
import com.pechuro.cashdebts.utils.AVATAR_PATH
import com.pechuro.cashdebts.utils.isExternalStorageWritable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ProfileEditFragmentViewModel @Inject constructor(
    private val userRepository: IUserRepository,
    private val storageRepository: IStorageRepository,
    private val appContext: Context,
    private val prefsManager: PrefsManager
) : BaseViewModel() {

    val command = PublishSubject.create<Events>()

    val data = ProfileEditModel()
    val localAvatarUri = ObservableField<Uri?>()
    val isLoading = ObservableBoolean()

    private var localAvatarFile: File? = null

    private var isUserAlreadyLoaded = false

    fun loadExistingUser() {
        if (isUserAlreadyLoaded) return
        command.onNext(Events.OnUserStartLoad)
        isUserAlreadyLoaded = true
        userRepository.get()
            .subscribe({
                data.setUser(it)
                command.onNext(Events.OnUserStopLoad)
            }, {
                command.onNext(Events.OnUserStopLoad)
                it.printStackTrace()
            }).addTo(compositeDisposable)
    }

    fun save() {
        if (!data.isValid()) return
        isLoading.set(true)
        val task = if (localAvatarFile != null) {
            storageRepository.uploadAndGetUrl(localAvatarFile!!.toUri(), localAvatarFile!!.name)
                .flatMapCompletable {
                    val user = FirestoreUser(
                        data.fields.firstName,
                        data.fields.lastName,
                        userRepository.currentUserBaseInformation.phoneNumber,
                        it.toString()
                    )
                    userRepository.setUser(user)
                }
        } else {
            val user = FirestoreUser(
                data.fields.firstName,
                data.fields.lastName,
                userRepository.currentUserBaseInformation.phoneNumber,
                data.fields.imageUrl.toString()
            )
            userRepository.setUser(user)
        }
        task.subscribe({
            onSaved()
        }, {
            it.printStackTrace()
        }).addTo(compositeDisposable)
    }

    fun loadEditedAvatar() {
        localAvatarUri.set(localAvatarFile?.toUri())
    }

    fun createImageFile() = try {
        createAvatarFile(appContext, "${userRepository.currentUserBaseInformation.uid}.jpg")?.also {
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

    private fun onSaved() {
        isLoading.set(false)
        command.onNext(Events.OnSaved)
        prefsManager.isUserAddInfo = true
    }

    sealed class Events : BaseEvent() {
        object OnUserStopLoad : Events()
        object OnUserStartLoad : Events()
        object OnSaved : Events()
    }
}