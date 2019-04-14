package com.pechuro.cashdebts.ui.utils.binding

import android.view.View
import com.google.android.material.chip.ChipGroup
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.data.model.DebtRole
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

fun Subject<Int>.receiveDebtRoleChangesFrom(view: ChipGroup): Disposable {
    view.findViewById<View>(view.checkedChipId).isClickable = false
    val listener = ChipGroup.OnCheckedChangeListener { _, id ->
        for (i in 0 until view.childCount) {
            val chip = view.getChildAt(i)
            chip.isClickable = chip.id != id
        }
        val role = when (id) {
            R.id.chip_creditor -> DebtRole.CREDITOR
            R.id.chip_debtor -> DebtRole.DEBTOR
            else -> DebtRole.CREDITOR
        }
        onNext(role)
    }
    view.setOnCheckedChangeListener(listener)

    return doOnDispose {
        view.setOnCheckedChangeListener(null)
    }
        .filter {
            val role = when (view.checkedChipId) {
                R.id.chip_creditor -> DebtRole.CREDITOR
                R.id.chip_debtor -> DebtRole.DEBTOR
                else -> DebtRole.CREDITOR
            }
            it != role
        }
        .subscribe {
            when (it) {
                DebtRole.CREDITOR -> {
                    view.check(R.id.chip_creditor)
                }
                DebtRole.DEBTOR -> {
                    view.check(R.id.chip_debtor)
                }
            }
        }
}
