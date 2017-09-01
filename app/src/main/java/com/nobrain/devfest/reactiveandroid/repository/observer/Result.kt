package com.nobrain.devfest.reactiveandroid.repository.observer


class Result<T> private constructor(var data: T, private val status: Status) {

    fun added(): Boolean {
        return status == Status.ADDED
    }

    fun updated(): Boolean {
        return status == Status.UPDATED
    }

    fun deleted(): Boolean {
        return status == Status.DELETED
    }

    private enum class Status {
        ADDED, UPDATED, DELETED, UNKNOWN
    }

    companion object {

        fun <T> added(t: T): Result<T> {
            return Result(t, Status.ADDED)
        }

        fun <T> updated(t: T): Result<T> {
            return Result(t, Status.UPDATED)
        }

        fun <T> deleted(t: T): Result<T> {
            return Result(t, Status.DELETED)
        }
    }
}
