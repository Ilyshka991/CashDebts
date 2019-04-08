package com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.data.model.FirestoreLocalDebt
import com.pechuro.cashdebts.ui.base.BaseViewHolder

class LocalDebtListAdapter : RecyclerView.Adapter<BaseViewHolder<FirestoreLocalDebt>>() {
    private val debts = mutableListOf<FirestoreLocalDebt>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<FirestoreLocalDebt> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_local_debt, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = debts.size

    override fun onBindViewHolder(holder: BaseViewHolder<FirestoreLocalDebt>, position: Int) =
        holder.onBind(debts[position])


    class ViewHolder(view: View) : BaseViewHolder<FirestoreLocalDebt>(view) {
        override fun onBind(data: FirestoreLocalDebt) {

        }
    }
}