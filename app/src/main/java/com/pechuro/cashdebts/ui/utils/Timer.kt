package com.pechuro.cashdebts.ui.utils

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class Timer(private val time: Long, startWith: Long = time) {

    private val timer =
        Observable.intervalRange(
            time - startWith,
            time + 1,
            0,
            1,
            TimeUnit.SECONDS,
            AndroidSchedulers.mainThread()
        )
            .map { time - it }
            .takeWhile { it != 0L }
            .doOnEach {
                lastTick = when {
                    it.isOnNext -> it.value!!
                    it.isOnComplete -> 0
                    else -> throw IllegalStateException()
                }

            }

    private lateinit var disposable: Disposable

    var lastTick = 0L
        private set

    fun start(onTick: (Long) -> Unit, onFinish: () -> Unit) {
        disposable = timer.subscribe(onTick, {}, onFinish)
    }

    fun stop() {
        disposable.dispose()
    }
}