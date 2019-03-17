package com.pechuro.cashdebts.ui.fragment.countyselection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.databinding.ItemCountryBinding
import com.pechuro.cashdebts.databinding.ItemCountryEmptyBinding
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.activity.countryselection.CountySelectEvent
import com.pechuro.cashdebts.ui.base.base.BaseViewHolder
import com.pechuro.cashdebts.ui.utils.EventBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class CountrySelectionAdapter(
    private val diffCallback: CountrySelectionDiffCallback,
    private val initialCountries: List<CountryData>
) : RecyclerView.Adapter<BaseViewHolder<CountryData>>() {

    private val countries = mutableListOf<CountryData>()

    private val searchSource = PublishSubject.create<String>()
    private val searchSubscriber = searchSource
        .debounce(100, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .map { query ->
            initialCountries.filter { it.name.startsWith(query, true) }
        }
        .map {
            diffCallback.setData(countries, it)
            val result = DiffUtil.calculateDiff(diffCallback)
            countries.clear()
            if (it.isEmpty()) {
                countries += CountryData.EMPTY
            } else {
                countries += it
            }
            result
        }
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            it.dispatchUpdatesTo(this)
        }

    init {
        countries += initialCountries
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        searchSubscriber.dispose()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<CountryData> = when (viewType) {
        VIEW_TYPE_EMPTY -> {
            val binding = ItemCountryEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            EmptyViewHolder(binding)
        }
        else -> {
            val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(binding)
        }
    }


    override fun getItemViewType(position: Int) = when {
        countries[position].isEmpty -> VIEW_TYPE_EMPTY
        else -> VIEW_TYPE_COMMON
    }

    override fun getItemCount() = countries.size

    override fun onBindViewHolder(holder: BaseViewHolder<CountryData>, position: Int) =
        holder.onBind(countries[position])

    fun filterCountries(query: String) {
        searchSource.onNext(query)
    }

    private class ViewHolder(private val binding: ItemCountryBinding) : BaseViewHolder<CountryData>(binding.root) {
        override fun onBind(data: CountryData) {
            binding.country = data
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                EventBus.publish(CountySelectEvent.OnCountySelect(data))
            }
        }
    }

    private class EmptyViewHolder(binding: ItemCountryEmptyBinding) : BaseViewHolder<CountryData>(binding.root) {
        override fun onBind(data: CountryData) {
        }
    }

    companion object ViewTypes {
        private const val VIEW_TYPE_COMMON = 1
        private const val VIEW_TYPE_EMPTY = 2
    }
}