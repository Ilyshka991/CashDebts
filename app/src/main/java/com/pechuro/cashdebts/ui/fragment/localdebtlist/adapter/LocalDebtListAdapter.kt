package com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseViewHolder
import com.pechuro.cashdebts.ui.fragment.localdebtlist.data.LocalDebt

class LocalDebtListAdapter : RecyclerView.Adapter<BaseViewHolder<LocalDebt>>() {
    private val debts = mutableListOf<LocalDebt>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LocalDebt> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_local_debt, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = debts.size

    override fun onBindViewHolder(holder: BaseViewHolder<LocalDebt>, position: Int) = holder.onBind(debts[position])


    class ViewHolder(view: View) : BaseViewHolder<LocalDebt>(view) {
        override fun onBind(data: LocalDebt) {

        }
    }
}