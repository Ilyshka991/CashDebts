package com.pechuro.cashdebts.ui.fragment.countyselection

import androidx.recyclerview.widget.DiffUtil
import com.pechuro.cashdebts.model.DiffResult
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.countyselection.model.CountrySelectionDiffCallback
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CountrySelectionFragmentViewModel @Inject constructor(
    private val countries: List<CountryData>,
    private val diffCallback: CountrySelectionDiffCallback
) : BaseViewModel() {

    val searchQuery = PublishSubject.create<String>()

    val countriesListSource: ConnectableObservable<DiffResult<CountryData>> =
        Observable.just(DiffResult(null, countries)).mergeWith(
            searchQuery
                .debounce(100, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .map { query ->
                    countries.filter { it.name.startsWith(query, true) }
                }
                .map {
                    val filteredList = mutableListOf<CountryData>()
                    if (it.isEmpty()) {
                        filteredList += CountryData.EMPTY
                    } else {
                        filteredList += it
                    }
                    filteredList
                }
                .map {
                    diffCallback.newList = it
                    val diffResult = DiffUtil.calculateDiff(diffCallback)
                    diffCallback.oldList = it
                    DiffResult(diffResult, it)
                })
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .replay(1)
}