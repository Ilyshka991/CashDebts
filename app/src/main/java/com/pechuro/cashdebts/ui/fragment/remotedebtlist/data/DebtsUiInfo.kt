package com.pechuro.cashdebts.ui.fragment.remotedebtlist.data

import com.pechuro.cashdebts.model.DiffResult

data class DebtsUiInfo(
    val diffResult: DiffResult<RemoteDebt>,
    val totalSum: Double
)