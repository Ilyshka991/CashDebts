package com.pechuro.cashdebts.ui.fragment.countyselection

import androidx.recyclerview.widget.DiffUtil
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.base.BaseViewModel
import com.pechuro.cashdebts.ui.fragment.countyselection.adapter.CountrySelectionDiffCallback
import com.pechuro.cashdebts.ui.fragment.countyselection.model.SearchResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CountrySelectionFragmentViewModel @Inject constructor(
    private val countries: List<CountryData>,
    private val diffCallback: CountrySelectionDiffCallback
) :
    BaseViewModel() {
    val searchQuery = PublishSubject.create<String>()
    val countriesList = BehaviorSubject.createDefault(SearchResult(null, countries))

    init {
        initSearch()
    }

    private fun initSearch() {
        searchQuery
            .debounce(100, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .map { query ->
                countries.filter { it.name.startsWith(query, true) }
            }
            .map {
                val filteredList = mutableListOf<CountryData>()
                if (filteredList.isEmpty()) {
                    filteredList += CountryData.EMPTY
                } else {
                    filteredList += it
                }
                diffCallback.setData(countriesList.value!!.dataList, filteredList)
                val diffResult = DiffUtil.calculateDiff(diffCallback)
                SearchResult(diffResult, filteredList)
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(countriesList)
    }
}