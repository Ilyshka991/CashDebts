package com.pechuro.cashdebts.ui.utils

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class BaseEvent

object EventBus {
    private val publishSubject = PublishSubject.create<BaseEvent>()

    fun publish(event: BaseEvent) {
        publishSubject.onNext(event)
    }

    fun <T : BaseEvent> listen(eventType: Class<T>): Observable<T> = publishSubject.ofType(eventType)
}
