package com.nobrain.devfest.reactiveandroid.repository


import com.nobrain.devfest.reactiveandroid.repository.domain.Todo
import com.nobrain.devfest.reactiveandroid.repository.observer.DataObserver
import com.nobrain.devfest.reactiveandroid.repository.observer.Result

import java.util.ArrayList

class TodoRepository private constructor() {
    private val todos: MutableList<Todo>

    init {
        todos = ArrayList()
    }

    fun getTodos(): List<Todo> {
        return todos
    }

    fun addTodo(id: Long, content: String) {
        val item = Todo(id)
        item.content = content
        todos.add(item)
        DataObserver.instance.notify(Result.added(item))

    }

    fun updateTodo(id: Long, content: String) {
        for (todo in todos) {
            if (todo.id == id) {
                todo.content = content
                DataObserver.instance.notify(Result.updated(todo))
                break
            }
        }
    }

    fun remove(id: Long) {
        var i = 0
        val todosSize = todos.size
        while (i < todosSize) {
            val todo = todos[i]
            if (todo.id == id) {
                todos.remove(todo)
                DataObserver.instance.notify(Result.deleted(todo))
                break
            }
            i++
        }
    }

    operator fun get(id: Long): Todo {
        for (todo in todos) {
            if (todo.id == id) {
                return todo
            }
        }
        return Todo(id)
    }

    private object Holder {
        val Instance = TodoRepository()
    }

    companion object {

        val instance: TodoRepository by lazy { Holder.Instance }

    }
}
