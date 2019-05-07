package com.pechuro.cashdebts.ui.fragment.localdebtlist.data

import com.pechuro.cashdebts.model.DiffResult

data class LocalDebtsUiInfo(
    val diffResult: DiffResult<LocalDebt>,
    val totalSum: Double
)