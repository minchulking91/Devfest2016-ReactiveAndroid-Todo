package com.nobrain.devfest.reactiveandroid.repository.observer


import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.*
import kotlin.reflect.KClass

class DataObserver private constructor() {

    private val notifier: Subject<Result<*>>
    private val observer: MutableMap<Any, CompositeDisposable>

    init {
        notifier = PublishSubject.create()
        observer = HashMap()
    }

    @Synchronized
    @Suppress("UNCHECKED_CAST")
    fun <DATA : Any> register(register: Any, domain: KClass<DATA>, notify: Notify<DATA>) {
        if (!observer.containsKey(register)) {
            observer.put(register, CompositeDisposable())
        }
        val compositeSubscription = observer[register]
        compositeSubscription?.add(notify.update(
                notifier.filter { domain.isInstance(it.data) }
                        .map { it as Result<DATA> }
        ))
    }

    @Synchronized
    fun unregister(register: Any) {
        if (observer.containsKey(register)) {
            observer.remove(register)?.clear()
        }
    }

    @Synchronized
    fun notify(result: Result<*>) {
        notifier.onNext(result)
    }


    private object Holder {
        val Instance = DataObserver()
    }

    companion object {

        val instance: DataObserver by lazy { Holder.Instance }

    }
}
