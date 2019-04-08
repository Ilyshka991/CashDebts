package com.pechuro.cashdebts.ui.fragment.localdebtlist.data

import androidx.recyclerview.widget.DiffUtil
import javax.inject.Inject

class LocalDebtDiffCallback @Inject constructor() : DiffUtil.Callback() {

    var oldList: List<LocalDebt> = emptyList()
    var newList: List<LocalDebt> = emptyList()

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}