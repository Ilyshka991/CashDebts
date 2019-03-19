package com.pechuro.cashdebts.ui.fragment.profileedit

import android.net.Uri
import androidx.core.net.toUri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.pechuro.cashdebts.data.model.FirestoreUser
import com.pechuro.cashdebts.data.repositories.IStorageRepository
import com.pechuro.cashdebts.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.files.FileManager
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.profileedit.model.ProfileEditModel
import com.pechuro.cashdebts.ui.utils.BaseEvent
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class ProfileEditFragmentViewModel @Inject constructor(
    private val userRepository: IUserRepository,
    private val storageRepository: IStorageRepository,
    private val prefsManager: PrefsManager,
    private val fileManager: FileManager
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
            if (data.fields.imageUrl != null) {
                storageRepository.deletePrevious(data.fields.imageUrl.toString())
                    .doOnComplete {
                        storageRepository.uploadAndGetUrl(
                            localAvatarFile!!.toUri(),
                            localAvatarFile!!.name
                        )
                            .flatMapCompletable {
                                val user = FirestoreUser(
                                    data.fields.firstName,
                                    data.fields.lastName,
                                    userRepository.currentUserBaseInformation.phoneNumber,
                                    it.toString()
                                )
                                userRepository.setUser(user)
                            }
                    }
            } else {
                storageRepository.uploadAndGetUrl(
                    localAvatarFile!!.toUri(),
                    localAvatarFile!!.name
                )
                    .flatMapCompletable {
                        val user = FirestoreUser(
                            data.fields.firstName,
                            data.fields.lastName,
                            userRepository.currentUserBaseInformation.phoneNumber,
                            it.toString()
                        )
                        userRepository.setUser(user)
                    }
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
        fileManager.createAvatarFile(userRepository.currentUserBaseInformation.uid)?.also {
            localAvatarFile = it
        }
    } catch (ex: IOException) {
        null
    }

    fun writeToFile(stream: InputStream) {
        val photoFile = createImageFile() ?: return
        fileManager.writeToFile(photoFile, stream)
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