package com.pechuro.cashdebts.ui.fragment.profileedit

import android.net.Uri
import androidx.core.net.toUri
import com.pechuro.cashdebts.data.data.model.FirestoreUser
import com.pechuro.cashdebts.data.data.repositories.IStorageRepository
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.connectivity.ConnectivityListener
import com.pechuro.cashdebts.model.files.FileManager
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.profileedit.model.ProfileEditModel
import com.pechuro.cashdebts.ui.utils.BaseEvent
import com.pechuro.cashdebts.ui.utils.extensions.requireValue
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
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

    val inputData = ProfileEditModel()

    val imageUrl = BehaviorSubject.createDefault("")
    val loadingState = BehaviorSubject.create<Boolean>()
    val isConnectionAvailable = BehaviorSubject.create<Boolean>()

    private var localAvatarFile: File? = null
    private var isUserAlreadyLoaded = false
    private var initialImageUrl = ""

    private var updateTask: Disposable? = null

    init {
        setConnectivityListener()
    }

    fun loadUser(isExist: Boolean = false) {
        if (isUserAlreadyLoaded) return
        if (!isExist) {
            isUserAlreadyLoaded = true
            return
        }
        command.onNext(Events.OnUserStartLoad)
        userRepository.getSingle()
            .subscribe({
                onUserLoaded(it)
                isUserAlreadyLoaded = true
                command.onNext(Events.OnUserStopLoad)
            }, {
                command.onNext(Events.OnUserStopLoad)
                if (isConnectionAvailable.value == true) {
                    command.onNext(Events.OnLoadError)
                }
            }).addTo(compositeDisposable)
    }


    fun save() {
        fun deletePreviousPhoto(): Completable {
            return storageRepository.delete(initialImageUrl)
        }

        fun uploadPhoto(): Single<Uri> {
            return storageRepository.uploadAndGetUrl(
                localAvatarFile!!.toUri(),
                localAvatarFile!!.name
            )
        }

        fun updateUser(photoUrl: String?): Completable {
            val user = FirestoreUser(
                inputData.fields.firstName.requireValue,
                inputData.fields.lastName.requireValue,
                userRepository.currentUserBaseInformation.phoneNumber,
                photoUrl
            )
            return userRepository.updateUser(user)
        }

        if (!inputData.isValid()) return
        loadingState.onNext(true)

        val task = if (imageUrl.value != initialImageUrl) {
            if (initialImageUrl.isNotEmpty()) {
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
            updateUser(initialImageUrl)
        }

        updateTask?.dispose()
        updateTask = task.subscribe({
            onSaved()
        }, {
            it.printStackTrace()
            loadingState.onNext(false)
            if (isConnectionAvailable.value == true) {
                command.onNext(Events.OnSaveError)
            }
        })
    }

    fun loadEditedAvatar() {
        localAvatarFile?.path?.let { imageUrl.onNext(it) }
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

    private fun onUserLoaded(user: FirestoreUser) {
        with(inputData.fields) {
            firstName.onNext(user.firstName)
            lastName.onNext(user.lastName)
        }

        initialImageUrl = user.photoUrl ?: ""
        imageUrl.onNext(user.photoUrl ?: "")
    }

    private fun onSaved() {
        loadingState.onNext(false)
        command.onNext(Events.OnSaved)
        prefsManager.isUserAddInfo = true
    }

    private fun setConnectivityListener() {
        connectivityListener.listen {
            onConnectionChanged(it)
        }.addTo(compositeDisposable)
    }

    private fun onConnectionChanged(isAvailable: Boolean) {
        isConnectionAvailable.onNext(isAvailable)
        if (isAvailable) {
            loadUser()
        } else {
            loadingState.onNext(false)
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