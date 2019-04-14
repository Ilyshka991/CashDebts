package com.pechuro.cashdebts.ui.fragment.countyselection.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.DiffResult
import com.pechuro.cashdebts.model.entity.CountryData
import com.pechuro.cashdebts.ui.base.BaseViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_country.view.*

class CountrySelectionAdapter : RecyclerView.Adapter<BaseViewHolder<CountryData>>() {

    private var countries = emptyList<CountryData>()

    private val _clicksEmitter = PublishSubject.create<CountryData>()
    val clickEmitter: Observable<CountryData> = _clicksEmitter

    private val onClickListener = View.OnClickListener {
        val data = it.tag as CountryData
        _clicksEmitter.onNext(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<CountryData> = when (viewType) {
        VIEW_TYPE_EMPTY -> {
            val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_country_empty, parent, false)
            EmptyViewHolder(binding)
        }
        else -> {
            val binding =
                LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
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

    fun updateCountries(result: DiffResult<CountryData>) {
        if (result.dataList == countries) return
        countries = result.dataList
        result.diffResult?.dispatchUpdatesTo(this) ?: notifyDataSetChanged()
    }

    private inner class ViewHolder(private val view: View) : BaseViewHolder<CountryData>(view) {
        override fun onBind(data: CountryData) {
            view.apply {
                text_country_name.text = data.name
                text_country_code.text = data.phonePrefix
                itemView.tag = data
                setOnClickListener(onClickListener)
            }
        }
    }

    private class EmptyViewHolder(view: View) : BaseViewHolder<CountryData>(view) {
        override fun onBind(data: CountryData) {}
    }

    companion object ViewTypes {
        private const val VIEW_TYPE_COMMON = 1
        private const val VIEW_TYPE_EMPTY = 2
    }
}