package com.pechuro.cashdebts.ui.fragment.countyselection.model

import androidx.recyclerview.widget.DiffUtil

class SearchResult<E : Any>(
    val diffResult: DiffUtil.DiffResult?,
    val dataList: List<E>
)