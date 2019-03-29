package com.pechuro.cashdebts.ui.fragment.profileedit

import com.pechuro.cashdebts.data.model.FirestoreUser
import com.pechuro.cashdebts.data.repositories.IStorageRepository
import com.pechuro.cashdebts.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.connectivity.ConnectivityListener
import com.pechuro.cashdebts.model.files.FileManager
import com.pechuro.cashdebts.model.prefs.PrefsManager
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.profileedit.model.ProfileEditModel
import com.pechuro.cashdebts.ui.utils.BaseEvent
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
    val initialUser = PublishSubject.create<FirestoreUser>()
    val isLoading = BehaviorSubject.create<Boolean>()
    val isConnectionAvailable = BehaviorSubject.create<Boolean>()

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
                initialUser.onNext(it)
                imageUrl.onNext(it.photoUrl ?: "")
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
      /*  fun deletePreviousPhoto(): Completable {
            return storageRepository.deletePrevious(imageUrl.value!!)
        }

        fun uploadPhoto(): Single<Uri> {
            return storageRepository.uploadAndGetUrl(
                localAvatarFile!!.toUri(),
                localAvatarFile!!.name
            )
        }

        fun updateUser(photoUrl: String?): Completable {
            val user = FirestoreUser(
                inputData.fields.firstName.value!!,
                inputData.fields.lastName.value!!,
                userRepository.currentUserBaseInformation.phoneNumber,
                photoUrl
            )
            return userRepository.updateUser(user)
        }

        if (!inputData.isValid()) return
        isLoading.onNext(true)

        val task = if (imageUrl.value!!.isNotEmpty()) {
            if (inputData.fields.imageUrl != null) {
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
            updateUser(inputData.fields.imageUrl)
        }

        updateTask = task.subscribe({
            onSaved()
        }, {
            isLoading.set(false)
            if (isConnectionAvailable.get()) {
                _commandSource.onNext(Events.OnSaveError)
            }
        })*/
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

    private fun onSaved() {
        isLoading.onNext(false)
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
            loadExistingUser()
        } else {
            isLoading.onNext(false)
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