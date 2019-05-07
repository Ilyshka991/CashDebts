package com.pechuro.cashdebts.ui.fragment.remotedebtlist.data

import com.pechuro.cashdebts.model.entity.DiffResult

data class RemoteDebtsUiInfo(
    val diffResult: DiffResult<RemoteDebt>,
    val totalSum: Double
)