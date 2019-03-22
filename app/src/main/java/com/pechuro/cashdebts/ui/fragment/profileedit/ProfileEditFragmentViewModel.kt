package com.pechuro.cashdebts.ui.fragment.profileedit

import android.net.Uri
import androidx.core.net.toUri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.pechuro.cashdebts.data.model.FirestoreUser
import com.pechuro.cashdebts.data.repositories.IStorageRepository
import com.pechuro.cashdebts.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.connectivity.ConnectivityListener
import com.pechuro.cashdebts.model.files.FileManager
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.profileedit.model.ProfileEditModel
import com.pechuro.cashdebts.ui.utils.BaseEvent
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
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
    private val fileManager: FileManager,
    private val connectivityListener: ConnectivityListener
) : BaseViewModel() {

    val command = PublishSubject.create<Events>()

    val data = ProfileEditModel()
    val localAvatarPath = ObservableField<String?>()
    val isLoading = ObservableBoolean()
    val isConnectionAvailable = ObservableBoolean()

    private var localAvatarFile: File? = null
    private var isUserAlreadyLoaded = false

    private var updateTask: Disposable? = null

    init {
        setConnectivityListener()
    }

    fun loadExistingUser() {
        if (isUserAlreadyLoaded) return
        command.onNext(Events.OnUserStartLoad)
        userRepository.get()
            .subscribe({
                data.setUser(it)
                isUserAlreadyLoaded = true
                command.onNext(Events.OnUserStopLoad)
            }, {
                command.onNext(Events.OnUserStopLoad)
                if (isConnectionAvailable.get()) {
                    command.onNext(Events.OnLoadError)
                }
            }).addTo(compositeDisposable)
    }

    fun save() {
        fun deletePreviousPhoto(): Completable {
            return storageRepository.deletePrevious(data.fields.imageUrl!!)
        }

        fun uploadPhoto(): Single<Uri> {
            return storageRepository.uploadAndGetUrl(
                localAvatarFile!!.toUri(),
                localAvatarFile!!.name
            )
        }

        fun updateUser(photoUrl: String?): Completable {
            val user = FirestoreUser(
                data.fields.firstName,
                data.fields.lastName,
                userRepository.currentUserBaseInformation.phoneNumber,
                photoUrl
            )
            return userRepository.updateUser(user)
        }

        if (!data.isValid()) return
        isLoading.set(true)

        val task = if (!localAvatarPath.get().isNullOrEmpty()) {
            if (data.fields.imageUrl != null) {
                uploadPhoto()
                    .flatMapCompletable {
                        updateUser(it.toString())
                    }.andThen(deletePreviousPhoto())
            } else {
                uploadPhoto().flatMapCompletable {
                    updateUser(it.toString())
                }
            }
        } else {
            updateUser(data.fields.imageUrl)
        }

        updateTask = task.subscribe({
            onSaved()
        }, {
            isLoading.set(false)
            if (isConnectionAvailable.get()) {
                command.onNext(Events.OnSaveError)
            }
        })
    }

    fun loadEditedAvatar() {
        localAvatarPath.set(localAvatarFile?.path)
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

    private fun setConnectivityListener() {
        connectivityListener.listen {
            onConnectionChanged(it)
        }.addTo(compositeDisposable)
    }

    private fun onConnectionChanged(isAvailable: Boolean) {
        isConnectionAvailable.set(isAvailable)
        if (isAvailable) {
            loadExistingUser()
        } else {
            isLoading.set(false)
            updateTask?.dispose()
        }
    }

    sealed class Events : BaseEvent() {
        object OnUserStopLoad : Events()
        object OnUserStartLoad : Events()
        object OnSaved : Events()
        object OnSaveError : Events()
        object OnLoadError : Events()
    }
}