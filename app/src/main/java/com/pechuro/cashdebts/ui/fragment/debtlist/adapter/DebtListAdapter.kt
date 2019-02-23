package com.pechuro.cashdebts.ui.fragment.debtlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.databinding.ItemDebtBinding
import com.pechuro.cashdebts.ui.base.BaseViewHolder
import com.pechuro.cashdebts.ui.base.BaseViewHolderData

class DebtListAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = ItemDebtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = 20

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

    }

    class ViewHolder(binding: ItemDebtBinding) : BaseViewHolder(binding.root) {
        override fun onBind(data: BaseViewHolderData) {

        }
    }
}