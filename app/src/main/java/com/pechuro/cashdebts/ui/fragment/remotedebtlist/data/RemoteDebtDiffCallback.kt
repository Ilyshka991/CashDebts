package com.pechuro.cashdebts.ui.fragment.remotedebtlist.data

import androidx.recyclerview.widget.DiffUtil
import javax.inject.Inject

class RemoteDebtDiffCallback @Inject constructor() : DiffUtil.Callback() {

    var oldList: List<RemoteDebt> = emptyList()
    var newList: List<RemoteDebt> = emptyList()

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] === newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}