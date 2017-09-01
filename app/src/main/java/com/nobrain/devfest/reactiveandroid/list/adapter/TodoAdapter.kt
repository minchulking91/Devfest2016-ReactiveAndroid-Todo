package com.nobrain.devfest.reactiveandroid.list.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nobrain.devfest.reactiveandroid.R
import com.nobrain.devfest.reactiveandroid.repository.domain.Todo
import com.nobrain.devfest.reactiveandroid.view.OnRecyclerItemClickListener
import java.util.*

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private val todoList: MutableList<Todo>

    var onRecyclerItemClickListener: OnRecyclerItemClickListener? = null

    init {
        todoList = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)

        holder.itemView.setOnClickListener { v ->
            if (onRecyclerItemClickListener != null) {
                onRecyclerItemClickListener!!.onItemClick(this@TodoAdapter, position)
            }
        }
    }

    fun getItem(position: Int): Todo {
        return todoList[position]
    }

    fun update(position: Int, item: Todo) {
        todoList[position] = item
    }

    fun add(todo: Todo) {
        todoList.add(todo)
    }

    fun remove(position: Int) {
        todoList.removeAt(position)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun clear() {
        todoList.clear()
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvTitle: TextView

        init {
            tvTitle = itemView.findViewById(R.id.tv_item_todo_summary)
        }

        fun bind(todo: Todo) {
            tvTitle.text = todo.content
        }
    }

}
