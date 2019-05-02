package com.pechuro.cashdebts.ui.fragment.profileview

import com.pechuro.cashdebts.data.data.model.FirestoreUser
import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.BaseEvent
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ProfileViewFragmentViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : BaseViewModel() {

    val loadingState = BehaviorSubject.create<LoadingState>()
    val user = BehaviorSubject.create<FirestoreUser>()

    init {
        loadUser()
    }

    fun loadUser() {
        loadingState.onNext(LoadingState.OnStart)
        userRepository.getSource()
            .subscribe({
                loadingState.onNext(LoadingState.OnStop)
                user.onNext(it)
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