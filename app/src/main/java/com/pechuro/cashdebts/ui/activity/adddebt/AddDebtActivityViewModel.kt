package com.pechuro.cashdebts.ui.activity.adddebt

import androidx.annotation.StringRes
import com.pechuro.cashdebts.data.FirestoreDebt
import com.pechuro.cashdebts.ui.base.BaseViewModel
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AddDebtActivityViewModel @Inject constructor() : BaseViewModel() {
    val debt = FirestoreDebt()
    val command = PublishSubject.create<Events>()

}

sealed class Events {
    object ShowInfoFragment : Events()
    object ShowUserfragment : Events()
    class ShowSnackBarError(@StringRes val id: Int) : Events()
}