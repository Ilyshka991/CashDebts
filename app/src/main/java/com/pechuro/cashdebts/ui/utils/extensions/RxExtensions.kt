package com.pechuro.cashdebts.ui.utils.extensions

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

inline val <T : Any> BehaviorSubject<T>.requireValue: T
    get() = value ?: throw NullPointerException()


//TODO: WTF??????????????
inline fun <T : Any> Observable<T>.mapIf(
    condition: Boolean,
    crossinline mapper: T.() -> T
): Observable<T> = map {
    println("Condition $condition")
    if (condition) mapper(it) else it
}

@SuppressLint("CheckResult")
fun main() {
    var condition = false
    Observable.interval(3, TimeUnit.SECONDS).map {
        it % 2 == 0L
    }.subscribe {
        condition = it
    }

    Observable.interval(1, TimeUnit.SECONDS).mapIf(condition) {
        this
    }.subscribe {
        println("Subscribe condition $condition")
    }
    Thread.sleep(20000)
}