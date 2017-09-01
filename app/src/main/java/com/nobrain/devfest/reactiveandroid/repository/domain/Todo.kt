package com.nobrain.devfest.reactiveandroid.repository.domain


class Todo {
    var id: Long = 0
    var content: String? = null

    constructor() {}

    constructor(id: Long) {
        this.id = id
    }
}
