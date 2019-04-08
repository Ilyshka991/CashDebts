package com.pechuro.cashdebts.model

import androidx.recyclerview.widget.DiffUtil

class DiffResult<E : Any>(
    val diffResult: DiffUtil.DiffResult?,
    val dataList: List<E>
)