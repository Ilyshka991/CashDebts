package com.pechuro.cashdebts.ui.utils

import io.reactivex.subjects.BehaviorSubject

inline val <T : Any> BehaviorSubject<T>.requireValue: T
    get() = value ?: throw NullPointerException()

