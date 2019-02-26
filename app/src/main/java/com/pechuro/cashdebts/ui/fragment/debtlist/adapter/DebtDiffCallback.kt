package com.pechuro.cashdebts.ui.fragment.debtlist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pechuro.cashdebts.data.model.Debt

class DebtDiffCallback : DiffUtil.Callback() {

    private var oldList: List<Debt> = emptyList()
    private var newList: List<Debt> = emptyList()

    fun setData(oldList: List<Debt>, newList: List<Debt>) {
        this.oldList = oldList
        this.newList = newList
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] === newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}