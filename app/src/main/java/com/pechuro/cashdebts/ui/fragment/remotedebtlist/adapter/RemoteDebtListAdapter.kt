package com.pechuro.cashdebts.ui.fragment.remotedebtlist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.data.model.DebtRole
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.COMPLETE
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.COMPLETION_REJECTED_BY_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.COMPLETION_REJECTED_BY_DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.CONFIRMATION_APPROVED
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.CONFIRMATION_REJECTED
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.NOT_SEND
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_COMPLETION_FROM_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_COMPLETION_FROM_DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_CONFIRMATION
import com.pechuro.cashdebts.model.DiffResult
import com.pechuro.cashdebts.ui.base.BaseViewHolder
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.data.RemoteDebt
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_remote_debt.view.*
import java.text.SimpleDateFormat
import javax.inject.Inject

class RemoteDebtListAdapter @Inject constructor(private val dateFormatter: SimpleDateFormat) :
    RecyclerView.Adapter<BaseViewHolder<RemoteDebt>>() {
    private var debtList = emptyList<RemoteDebt>()

    private val _longClickEmitter = PublishSubject.create<RemoteDebt.User>()
    val longClickEmitter: Observable<RemoteDebt.User> = _longClickEmitter

    private val onClickListener = View.OnClickListener {
        val itemInfo = it.tag as? ItemInfo ?: return@OnClickListener
        with(itemInfo) {
            data.isExpanded = !data.isExpanded
            notifyItemChanged(viewHolder.adapterPosition)
        }
    }
    private val onLongClickListener = View.OnLongClickListener {
        val itemInfo = it.tag as? ItemInfo ?: return@OnLongClickListener true
        _longClickEmitter.onNext(itemInfo.data.user)
        true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<RemoteDebt> = when (viewType) {
        VIEW_TYPE_COMMON -> {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_remote_debt, parent, false)
            ViewHolder(view)
        }
        VIEW_TYPE_EMPTY -> {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_debt_empty, parent, false)
            EmptyViewHolder(view)
        }
        else -> throw IllegalArgumentException()
    }

    override fun getItemCount() = debtList.size

    override fun getItemViewType(position: Int) = when {
        debtList[position].isEmpty() -> VIEW_TYPE_EMPTY
        else -> VIEW_TYPE_COMMON
    }

    override fun onBindViewHolder(holder: BaseViewHolder<RemoteDebt>, position: Int) = holder.onBind(debtList[position])

    fun update(result: DiffResult<RemoteDebt>) {
        when {
            debtList.isEmpty() -> {
                debtList = result.dataList
                notifyDataSetChanged()
            }
            debtList == result.dataList -> return
            else -> {
                debtList = result.dataList
                result.diffResult?.dispatchUpdatesTo(this) ?: notifyDataSetChanged()
            }
        }
    }

    private inner class ViewHolder(private val view: View) : BaseViewHolder<RemoteDebt>(view) {

        @SuppressLint("SetTextI18n")
        override fun onBind(data: RemoteDebt) {
            view.apply {
                if ((itemView.tag as? ItemInfo)?.data === data) {
                    if (data.description.isNotEmpty()) text_description.isVisible = data.isExpanded
                    text_date.isVisible = data.isExpanded
                    return
                }

                text_person_name.text = "${data.user.firstName} ${data.user.lastName}"

                val textValueStringRes = when (data.role) {
                    DebtRole.CREDITOR -> R.string.item_local_debt_msg_creditor
                    DebtRole.DEBTOR -> R.string.item_local_debt_msg_debtor
                    else -> throw IllegalArgumentException()
                }
                val textValue = context.getString(textValueStringRes, data.value)
                text_value.text = textValue

                val textStatusRes = when (data.status) {
                    NOT_SEND -> R.string.debt_status_not_send
                    WAIT_FOR_CONFIRMATION -> R.string.debt_status_wait_for_confirmation
                    CONFIRMATION_REJECTED -> R.string.debt_status_confirmation_rejected
                    CONFIRMATION_APPROVED -> R.string.debt_status_confirmation_approved
                    WAIT_FOR_COMPLETION_FROM_CREDITOR -> R.string.debt_status_wait_for_completion
                    WAIT_FOR_COMPLETION_FROM_DEBTOR -> R.string.debt_status_wait_for_completion
                    COMPLETION_REJECTED_BY_CREDITOR -> R.string.debt_status_completion_rejected
                    COMPLETION_REJECTED_BY_DEBTOR -> R.string.debt_status_completion_rejected
                    COMPLETE -> R.string.debt_status_complete
                    else -> throw IllegalArgumentException()
                }
                val textStatusString = context.getString(textStatusRes)
                val resultStatusText = context.getString(R.string.debt_status_msg, textStatusString)
                text_status.text = resultStatusText

                text_description.apply {
                    isVisible = data.isExpanded
                    if (data.description.isEmpty()) isVisible = false else text = data.description
                }

                text_date.apply {
                    text = dateFormatter.format(data.date)
                    isVisible = data.isExpanded
                }

                itemView.tag = ItemInfo(data, this@ViewHolder)
                setOnClickListener(onClickListener)
                setOnLongClickListener(onLongClickListener)
            }
        }
    }

    private class EmptyViewHolder(view: View) : BaseViewHolder<RemoteDebt>(view) {
        override fun onBind(data: RemoteDebt) {}
    }

    private class ItemInfo(
        val data: RemoteDebt,
        val viewHolder: RemoteDebtListAdapter.ViewHolder
    )

    companion object ViewTypes {
        private const val VIEW_TYPE_COMMON = 1
        private const val VIEW_TYPE_EMPTY = 2
    }
}