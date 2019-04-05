package com.pechuro.cashdebts.ui.fragment.profileview

import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.utils.BaseEvent
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ProfileViewFragmentViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : BaseViewModel() {

    val loadingState = BehaviorSubject.create<LoadingState>()

    private val _user = BehaviorSubject.create<com.pechuro.cashdebts.data.data.model.FirestoreUser>()
    val user: Observable<com.pechuro.cashdebts.data.data.model.FirestoreUser> = _user

    init {
        loadUser()
    }

    fun loadUser() {
        loadingState.onNext(LoadingState.OnStart)
        userRepository.get()
            .subscribe({
                _user.onNext(it)
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