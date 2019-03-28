package com.pechuro.cashdebts.ui.fragment.countyselection.model

import androidx.recyclerview.widget.DiffUtil
import com.pechuro.cashdebts.model.entity.CountryData
import javax.inject.Inject

class CountrySelectionDiffCallback @Inject constructor(initialList: List<CountryData>) : DiffUtil.Callback() {

    var oldList: List<CountryData> = initialList
    var newList: List<CountryData> = emptyList()

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] === newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].code == newList[newItemPosition].code
}