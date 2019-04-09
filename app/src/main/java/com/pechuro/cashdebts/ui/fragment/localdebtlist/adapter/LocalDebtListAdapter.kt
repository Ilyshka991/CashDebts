package com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.data.model.DebtRole
import com.pechuro.cashdebts.model.DiffResult
import com.pechuro.cashdebts.ui.base.BaseViewHolder
import com.pechuro.cashdebts.ui.fragment.localdebtlist.data.LocalDebt
import kotlinx.android.synthetic.main.item_local_debt.view.*
import java.text.SimpleDateFormat
import javax.inject.Inject

class LocalDebtListAdapter @Inject constructor(private val dateFormatter: SimpleDateFormat) :
    RecyclerView.Adapter<BaseViewHolder<LocalDebt>>() {
    private var debts: List<LocalDebt> = emptyList()

    private val onClickListener = View.OnClickListener {
        val viewHolder = it.tag as ViewHolder
        viewHolder.isExpanded = !viewHolder.isExpanded
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LocalDebt> = when (viewType) {
        VIEW_TYPE_COMMON -> {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_local_debt, parent, false)
            ViewHolder(view)
        }
        VIEW_TYPE_EMPTY -> {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_local_debt_empty, parent, false)
            EmptyViewHolder(view)
        }
        else -> throw IllegalArgumentException()
    }

    override fun getItemCount() = debts.size

    override fun getItemViewType(position: Int) = when {
        debts[position].isEmpty() -> VIEW_TYPE_EMPTY
        else -> VIEW_TYPE_COMMON
    }

    override fun onBindViewHolder(holder: BaseViewHolder<LocalDebt>, position: Int) =
        holder.onBind(debts[position])

    fun update(result: DiffResult<LocalDebt>) {
        debts = result.dataList
        result.diffResult?.dispatchUpdatesTo(this) ?: notifyDataSetChanged()
    }

    inner class ViewHolder(private val view: View) : BaseViewHolder<LocalDebt>(view) {
        var isExpanded = false
            set(value) {
                field = value
                view.text_description.isVisible = if (view.text_description.text.isNullOrEmpty()) false else value
                view.text_date.isVisible = value
            }

        override fun onBind(data: LocalDebt) {
            view.apply {
                text_debtor.text = data.personName

                val textValueStringRes = when (data.role) {
                    DebtRole.CREDITOR -> R.string.item_local_debt_msg_creditor
                    DebtRole.DEBTOR -> R.string.item_local_debt_msg_debtor
                    else -> throw IllegalArgumentException()
                }
                val textValue = view.context.getString(textValueStringRes, data.value)
                text_value.text = textValue

                text_description.text = data.description

                text_date.text = dateFormatter.format(data.date)

                view.tag = this@ViewHolder
                isExpanded = false
                setOnClickListener(onClickListener)
            }
        }
    }

    class EmptyViewHolder(view: View) : BaseViewHolder<LocalDebt>(view) {
        override fun onBind(data: LocalDebt) {}
    }

    companion object ViewTypes {
        private const val VIEW_TYPE_COMMON = 1
        private const val VIEW_TYPE_EMPTY = 2
    }
}