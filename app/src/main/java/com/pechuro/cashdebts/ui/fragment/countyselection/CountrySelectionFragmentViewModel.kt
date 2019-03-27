package com.pechuro.cashdebts.ui.fragment.countyselection

import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CountrySelectionFragmentViewModel @Inject constructor(private val countries: List<CountryData>) :
    BaseViewModel() {
    val searchSource = BehaviorSubject.createDefault("")

    val searchSubscriber = searchSource
        .debounce(100, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .map { query ->
            countries.filter { it.name.startsWith(query, true) }
        }
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())

}