package com.pechuro.cashdebts.ui.utils

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseEvent

object EventManager {
    private val publishSubject = PublishSubject.create<BaseEvent>()
    private val behaviorSubject = BehaviorSubject.create<BaseEvent>()

    fun publish(event: BaseEvent, withReplay: Boolean = false) {
        if (withReplay) behaviorSubject.onNext(event) else publishSubject.onNext(event)
    }

    fun <T : BaseEvent> listen(eventType: Class<T>, withReplay: Boolean = false): Observable<T> =
        if (withReplay) behaviorSubject.ofType(eventType) else publishSubject.ofType(eventType)

}
