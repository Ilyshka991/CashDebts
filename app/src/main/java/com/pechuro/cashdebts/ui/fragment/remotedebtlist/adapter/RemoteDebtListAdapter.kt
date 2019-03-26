package com.pechuro.cashdebts.ui.fragment.remotedebtlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseViewHolder
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.data.RemoteDebt

class RemoteDebtListAdapter : RecyclerView.Adapter<BaseViewHolder<RemoteDebt>>() {
    private val debts = mutableListOf<RemoteDebt>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<RemoteDebt> {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_remote_debt, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = debts.size

    override fun onBindViewHolder(holder: BaseViewHolder<RemoteDebt>, position: Int) = holder.onBind(debts[position])


    class ViewHolder(view: View) : BaseViewHolder<RemoteDebt>(view) {
        override fun onBind(data: RemoteDebt) {
        }
    }
}