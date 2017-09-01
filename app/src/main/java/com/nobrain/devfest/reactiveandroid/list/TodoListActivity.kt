package com.nobrain.devfest.reactiveandroid.list


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.nobrain.devfest.reactiveandroid.R
import com.nobrain.devfest.reactiveandroid.detail.TodoDetailActivity
import com.nobrain.devfest.reactiveandroid.list.adapter.TodoAdapter
import com.nobrain.devfest.reactiveandroid.repository.TodoRepository
import com.nobrain.devfest.reactiveandroid.repository.domain.Todo
import com.nobrain.devfest.reactiveandroid.repository.observer.DataObserver
import com.nobrain.devfest.reactiveandroid.repository.observer.Notify
import com.nobrain.devfest.reactiveandroid.repository.observer.Result
import com.nobrain.devfest.reactiveandroid.view.OnRecyclerItemClickListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.act_todo_list.*

class TodoListActivity : AppCompatActivity() {


    private lateinit var adapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_todo_list)


        initListView()
        initTodoDatas()



        DataObserver.instance.register(this, Todo::class, object : Notify<Todo> {
            override fun update(observable: Observable<Result<Todo>>): Disposable {
                return observable.filter { it.added() }
                        .map { it.data }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            adapter.add(it)
                            adapter.notifyDataSetChanged()
                        }
            }
        })
        DataObserver.instance.register(this, Todo::class, object : Notify<Todo> {
            override fun update(observable: Observable<Result<Todo>>): Disposable {
                return observable.filter { it.updated() }
                        .map { it.data }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            for (idx in 0 until adapter.itemCount) {
                                if (adapter.getItem(idx).id == it.id) {
                                    adapter.getItem(idx).content = it.content
                                    break
                                }
                            }
                            adapter.notifyDataSetChanged()
                        }
            }
        })
        DataObserver.instance.register(this, Todo::class, object : Notify<Todo> {
            override fun update(observable: Observable<Result<Todo>>): Disposable {
                return observable.filter { it.deleted() }
                        .map { it.data }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            for (idx in 0 until adapter.itemCount) {
                                if (adapter.getItem(idx).id == it.id) {
                                    adapter.remove(idx)
                                    break
                                }
                            }
                            adapter.notifyDataSetChanged()
                        }
            }
        })

    }

    override fun onDestroy() {
        DataObserver.instance.unregister(this)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu_add) {

            startActivity(Intent(this, TodoDetailActivity::class.java))

            return true
        }
        return false
    }

    private fun initListView() {
        adapter = TodoAdapter()
        list_todo_list.adapter = adapter
        list_todo_list.layoutManager = LinearLayoutManager(this@TodoListActivity)

        adapter.onRecyclerItemClickListener = object : OnRecyclerItemClickListener {
            override fun onItemClick(adapter: RecyclerView.Adapter<*>, position: Int) {
                if (adapter is TodoAdapter) {
                    val id = adapter.getItem(position).id
                    val intent = Intent(this@TodoListActivity, TodoDetailActivity::class.java)
                    intent.putExtra("id", id)
                    startActivity(intent)
                }
            }

        }
    }

    private fun initTodoDatas() {

        adapter.clear()

        val todos = TodoRepository.instance.getTodos()
        for (todo in todos) {
            adapter.add(todo)
        }

        adapter.notifyDataSetChanged()
    }
}
