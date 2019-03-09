package com.pechuro.cashdebts.ui.activity.auth.countyselect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.databinding.ItemCountryBinding
import com.pechuro.cashdebts.ui.base.BaseViewHolder
import com.pechuro.cashdebts.ui.custom.phone.CountryData
import com.pechuro.cashdebts.ui.utils.EventBus

class CountrySelectAdapter(private val countries: List<CountryData>) :
    RecyclerView.Adapter<BaseViewHolder<CountryData>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<CountryData> {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = countries.size

    override fun onBindViewHolder(holder: BaseViewHolder<CountryData>, position: Int) =
        holder.onBind(countries[position])

    class ViewHolder(private val binding: ItemCountryBinding) : BaseViewHolder<CountryData>(binding.root) {
        override fun onBind(data: CountryData) {
            binding.country = data
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                EventBus.publish(CountySelectEvent.OnCountySelect(data))
            }
        }
    }
}