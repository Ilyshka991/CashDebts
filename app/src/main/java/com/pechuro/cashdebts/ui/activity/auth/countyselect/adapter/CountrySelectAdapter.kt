package com.pechuro.cashdebts.ui.activity.auth.countyselect.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.databinding.ItemCountryBinding
import com.pechuro.cashdebts.ui.activity.auth.countyselect.CountySelectEvent
import com.pechuro.cashdebts.ui.base.BaseViewHolder
import com.pechuro.cashdebts.ui.custom.phone.CountryData
import com.pechuro.cashdebts.ui.utils.EventBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class CountrySelectAdapter(
    private val diffCallback: CountrySelectDiffCallback,
    private val initialCountries: List<CountryData>
) : RecyclerView.Adapter<BaseViewHolder<CountryData>>() {

    private val countries = mutableListOf<CountryData>()

    private val searchSource = PublishSubject.create<String>()
    private val searchSubscriber = searchSource
        .map { query ->
            initialCountries.filter { it.name.startsWith(query, true) }
        }
        .map {
            diffCallback.setData(countries, it)
            val result = DiffUtil.calculateDiff(diffCallback)
            countries.clear()
            countries += it
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<CountryData> {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = countries.size

    override fun onBindViewHolder(holder: BaseViewHolder<CountryData>, position: Int) =
        holder.onBind(countries[position])

    fun filterCountries(query: String) {
        searchSource.onNext(query)
    }

    class ViewHolder(private val binding: ItemCountryBinding) : BaseViewHolder<CountryData>(binding.root) {
        override fun onBind(data: CountryData) {
            binding.country = data
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                EventBus.publish(
                    CountySelectEvent.OnCountySelect(
                        data
                    )
                )
            }
        }
    }
}