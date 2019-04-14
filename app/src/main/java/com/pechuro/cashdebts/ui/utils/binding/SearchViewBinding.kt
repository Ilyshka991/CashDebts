package com.pechuro.cashdebts.ui.utils.binding

import androidx.appcompat.widget.SearchView
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

fun Subject<String>.receiveQueryChangesFrom(view: SearchView): Disposable {
    val listener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = false

        override fun onQueryTextChange(newText: String?): Boolean {
            onNext(newText ?: "")
            return true
        }
    }
    view.setOnQueryTextListener(listener)

    return doOnDispose {
        view.setOnQueryTextListener(null)
    }
        .filter { view.query.toString() != it }
        .subscribe {
            view.setQuery(it, false)
        }
}
