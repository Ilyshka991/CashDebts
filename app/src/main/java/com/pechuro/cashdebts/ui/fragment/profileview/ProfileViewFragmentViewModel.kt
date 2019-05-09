package com.pechuro.cashdebts.ui.fragment.profileview

import com.pechuro.cashdebts.data.data.repositories.IUserRepository
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.profileview.data.ProfileUser
import com.pechuro.cashdebts.ui.utils.BaseEvent
import com.pechuro.cashdebts.ui.utils.extensions.getFormattedNumber
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ProfileViewFragmentViewModel @Inject constructor(
    private val userRepository: IUserRepository,
    private val countryList: List<CountryData>
) : BaseViewModel() {

    val loadingState = BehaviorSubject.create<LoadingState>()
    val user = BehaviorSubject.create<ProfileUser>()

    init {
        loadUser()
    }

    fun loadUser() {
        loadingState.onNext(LoadingState.OnStart)
        userRepository.getSource()
            .subscribe({
                loadingState.onNext(LoadingState.OnStop)
                user.onNext(
                    ProfileUser(
                        it.firstName,
                        it.lastName,
                        it.phoneNumber.getFormattedNumber(countryList),
                        it.photoUrl
                    )
                )
            }, {
                loadingState.onNext(LoadingState.OnStop)
                loadingState.onNext(LoadingState.OnError)
            }).addTo(compositeDisposable)
    }

    sealed class LoadingState : BaseEvent() {
        object OnStart : LoadingState()
        object OnStop : LoadingState()
        object OnError : LoadingState()
    }
}