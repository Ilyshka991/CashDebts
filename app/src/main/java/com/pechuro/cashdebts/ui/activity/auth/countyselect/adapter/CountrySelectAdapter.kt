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

class CountrySelectAdapter(
    private val diffCallback: CountrySelectDiffCallback,
    initialCountries: List<CountryData>
) : RecyclerView.Adapter<BaseViewHolder<CountryData>>() {

    private val countries = mutableListOf(*initialCountries.toTypedArray())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<CountryData> {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = countries.size

    override fun onBindViewHolder(holder: BaseViewHolder<CountryData>, position: Int) =
        holder.onBind(countries[position])

    fun setCountries(data: List<CountryData>) {
        diffCallback.setData(countries, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        countries.clear()
        countries += data
        diffResult.dispatchUpdatesTo(this)
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