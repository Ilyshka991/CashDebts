package com.pechuro.cashdebts.ui.fragment.remotedebtlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.databinding.ItemRemoteDebtBinding
import com.pechuro.cashdebts.ui.base.base.BaseViewHolder
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.data.RemoteDebt

class RemoteDebtListAdapter : RecyclerView.Adapter<BaseViewHolder<RemoteDebt>>() {
    private val debts = mutableListOf<RemoteDebt>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<RemoteDebt> {
        val binding = ItemRemoteDebtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = debts.size

    override fun onBindViewHolder(holder: BaseViewHolder<RemoteDebt>, position: Int) = holder.onBind(debts[position])


    class ViewHolder(private val binding: ItemRemoteDebtBinding) : BaseViewHolder<RemoteDebt>(binding.root) {
        override fun onBind(data: RemoteDebt) {
            binding.debt = data
            binding.executePendingBindings()
        }
    }
}