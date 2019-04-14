package com.pechuro.cashdebts.ui.fragment.profileview

import com.pechuro.cashdebts.data.data.model.FirestoreUser
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

    private val _loadingState = BehaviorSubject.create<LoadingState>()
    val loadingState: Observable<LoadingState> = _loadingState

    private val _user = BehaviorSubject.create<FirestoreUser>()
    val user: Observable<FirestoreUser> = _user

    init {
        loadUser()
    }

    fun loadUser() {
        _loadingState.onNext(LoadingState.OnStart)
        userRepository.get()
            .subscribe({
                _user.onNext(it)
                _loadingState.onNext(LoadingState.OnStop)
            }, {
                _loadingState.onNext(LoadingState.OnStop)
                _loadingState.onNext(LoadingState.OnError)
                it.printStackTrace()
            }).addTo(compositeDisposable)
    }

    sealed class LoadingState : BaseEvent() {
        object OnStart : LoadingState()
        object OnStop : LoadingState()
        object OnError : LoadingState()
    }
}