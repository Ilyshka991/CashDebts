package com.pechuro.cashdebts.ui.fragment.profileview

import androidx.databinding.ObservableField
import com.pechuro.cashdebts.data.model.FirestoreUser
import com.pechuro.cashdebts.data.repositories.IUserRepository
import com.pechuro.cashdebts.ui.base.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.BaseEvent
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ProfileViewFragmentViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : BaseViewModel() {

    val loadingState = BehaviorSubject.create<LoadingState>()
    val user = ObservableField<FirestoreUser>()

    init {
        loadUser()
    }

    fun loadUser() {
        loadingState.onNext(LoadingState.OnStart)
        userRepository.get()
            .subscribe({
                user.set(it)
                loadingState.onNext(LoadingState.OnStop)
            }, {
                loadingState.onNext(LoadingState.OnStop)
                loadingState.onNext(LoadingState.OnError)
                it.printStackTrace()
            }).addTo(compositeDisposable)
    }

    sealed class LoadingState : BaseEvent() {
        object OnStart : LoadingState()
        object OnStop : LoadingState()
        object OnError : LoadingState()
    }
}