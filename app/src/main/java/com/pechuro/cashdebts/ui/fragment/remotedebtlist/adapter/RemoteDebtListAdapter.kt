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
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.CONFIRMATION_REJECTED
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.EDIT_CONFIRMATION_REJECTED_BY_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.EDIT_CONFIRMATION_REJECTED_BY_DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.IN_PROGRESS
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.NOT_SEND
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_COMPLETE_FROM_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_COMPLETE_FROM_DEBTOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_CONFIRMATION
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_EDIT_CONFIRMATION_FROM_CREDITOR
import com.pechuro.cashdebts.data.data.model.FirestoreDebtStatus.Companion.WAIT_FOR_EDIT_CONFIRMATION_FROM_DEBTOR
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

    private val _actionsClickEmitter = PublishSubject.create<Pair<Actions, RemoteDebt>>()
    val actionsClickEmitter: Observable<Pair<Actions, RemoteDebt>> = _actionsClickEmitter

    private val onItemClickListener = View.OnClickListener {
        val itemInfo = it.tag as? ItemInfo ?: return@OnClickListener
        with(itemInfo) {
            data.isExpanded = !data.isExpanded
            notifyItemChanged(viewHolder.adapterPosition)
        }
    }
    private val onActionsClickListener = View.OnClickListener {
        val itemInfo = it.tag as? RemoteDebt ?: return@OnClickListener
        val action = when (it.id) {
            R.id.button_reject -> Actions.REJECT
            R.id.button_accept -> Actions.ACCEPT
            R.id.button_ok -> Actions.OK
            R.id.button_delete -> Actions.DELETE
            else -> throw IllegalArgumentException()
        }
        _actionsClickEmitter.onNext(action to itemInfo)
    }
    private val onLongClickListener = View.OnLongClickListener {
        val itemInfo = it.tag as? ItemInfo ?: return@OnLongClickListener true
        _longClickEmitter.onNext(itemInfo.data.user)
        true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<RemoteDebt> =
        when (viewType) {
            VIEW_TYPE_COMMON -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_remote_debt, parent, false)
                ViewHolder(view)
            }
            VIEW_TYPE_EMPTY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_debt_empty, parent, false)
                EmptyViewHolder(view)
            }
            else -> throw IllegalArgumentException()
        }

    override fun getItemCount() = debtList.size

    override fun getItemViewType(position: Int) = when {
        debtList[position].isEmpty() -> VIEW_TYPE_EMPTY
        else -> VIEW_TYPE_COMMON
    }

    override fun onBindViewHolder(holder: BaseViewHolder<RemoteDebt>, position: Int) =
        holder.onBind(debtList[position])

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

    override fun getItemId(position: Int) = debtList[position].id.hashCode().toLong()

    fun getItemByPosition(position: Int) = debtList[position]

    private inner class ViewHolder(private val view: View) : BaseViewHolder<RemoteDebt>(view) {

        @SuppressLint("SetTextI18n")
        override fun onBind(data: RemoteDebt) {
            view.apply {
                if ((itemView.tag as? ItemInfo)?.data === data) {
                    val isExpanded = data.isExpanded

                    if (data.description.isNotEmpty()) text_description.isVisible = isExpanded
                    text_date.isVisible = isExpanded
                    if (data.status == COMPLETE) button_delete.isVisible = isExpanded

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

                var isActionButtonsVisible = false
                var isOkButtonVisible = false
                var isDeleteButtonVisible = false
                isSwipeable = false
                val textStatusRes = when (data.status) {
                    NOT_SEND -> R.string.debt_status_not_send
                    WAIT_FOR_CONFIRMATION -> if (data.isCurrentUserInit) {
                        R.string.debt_status_wait_for_confirmation
                    } else {
                        isActionButtonsVisible = true
                        R.string.debt_status_need_add_approve
                    }
                    CONFIRMATION_REJECTED -> {
                        if (data.isCurrentUserInit) isOkButtonVisible = true
                        R.string.debt_status_confirmation_rejected
                    }
                    IN_PROGRESS -> {
                        isSwipeable = true
                        0
                    }
                    WAIT_FOR_COMPLETE_FROM_CREDITOR -> if (data.role == DebtRole.CREDITOR) {
                        isActionButtonsVisible = true
                        R.string.debt_status_need_completion_approve
                    } else {
                        R.string.debt_status_wait_for_completion
                    }
                    WAIT_FOR_COMPLETE_FROM_DEBTOR -> if (data.role == DebtRole.DEBTOR) {
                        isActionButtonsVisible = true
                        R.string.debt_status_need_completion_approve
                    } else {
                        R.string.debt_status_wait_for_completion
                    }
                    COMPLETION_REJECTED_BY_CREDITOR -> {
                        if (data.role == DebtRole.DEBTOR) isOkButtonVisible = true
                        R.string.debt_status_completion_rejected
                    }
                    COMPLETION_REJECTED_BY_DEBTOR -> {
                        if (data.role == DebtRole.CREDITOR) isOkButtonVisible = true
                        R.string.debt_status_completion_rejected
                    }
                    COMPLETE -> {
                        if (data.isExpanded) isDeleteButtonVisible = true
                        R.string.debt_status_complete
                    }
                    WAIT_FOR_EDIT_CONFIRMATION_FROM_CREDITOR -> if (data.role == DebtRole.CREDITOR) {
                        isActionButtonsVisible = true
                        R.string.debt_status_need_edit_confirmation
                    } else {
                        R.string.debt_status_wait_for_edit_confirmation
                    }
                    WAIT_FOR_EDIT_CONFIRMATION_FROM_DEBTOR -> if (data.role == DebtRole.DEBTOR) {
                        isActionButtonsVisible = true
                        R.string.debt_status_need_edit_confirmation
                    } else {
                        R.string.debt_status_wait_for_edit_confirmation
                    }
                    EDIT_CONFIRMATION_REJECTED_BY_CREDITOR -> {
                        if (data.role == DebtRole.DEBTOR) isOkButtonVisible = true
                        R.string.debt_status_edit_rejected
                    }
                    EDIT_CONFIRMATION_REJECTED_BY_DEBTOR -> {
                        if (data.role == DebtRole.CREDITOR) isOkButtonVisible = true
                        R.string.debt_status_edit_rejected
                    }
                    else -> throw IllegalArgumentException()
                }
                if (textStatusRes == 0) {
                    text_status.isVisible = false
                } else {
                    text_status.isVisible = true
                    text_status.setText(
                        textStatusRes
                    )
                }
                button_accept.isVisible = isActionButtonsVisible
                button_reject.isVisible = isActionButtonsVisible
                button_ok.isVisible = isOkButtonVisible
                button_delete.isVisible = isDeleteButtonVisible

                text_description.apply {
                    isVisible = data.isExpanded
                    if (data.description.isEmpty()) isVisible = false else text = data.description
                }

                text_date.apply {
                    text = dateFormatter.format(data.date)
                    isVisible = data.isExpanded
                }

                itemView.tag = ItemInfo(data, this@ViewHolder)
                setOnClickListener(onItemClickListener)
                setOnLongClickListener(onLongClickListener)

                button_accept.apply {
                    tag = data
                    setOnClickListener(onActionsClickListener)
                }
                button_reject.apply {
                    tag = data
                    setOnClickListener(onActionsClickListener)
                }
                button_ok.apply {
                    tag = data
                    setOnClickListener(onActionsClickListener)
                }
                button_delete.apply {
                    tag = data
                    setOnClickListener(onActionsClickListener)
                }
            }
        }
    }

    private class EmptyViewHolder(view: View) : BaseViewHolder<RemoteDebt>(view) {
        override fun onBind(data: RemoteDebt) {
            isSwipeable = false
        }
    }

    private class ItemInfo(
        val data: RemoteDebt,
        val viewHolder: RemoteDebtListAdapter.ViewHolder
    )

    enum class Actions {
        ACCEPT, REJECT, OK, DELETE
    }

    companion object ViewTypes {
        private const val VIEW_TYPE_COMMON = 1
        private const val VIEW_TYPE_EMPTY = 2
    }
}