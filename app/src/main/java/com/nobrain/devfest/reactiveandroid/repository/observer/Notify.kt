package com.nobrain.devfest.reactiveandroid.repository.observer


import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface Notify<T> {
    fun update(observable: Observable<Result<T>>): Disposable
}
