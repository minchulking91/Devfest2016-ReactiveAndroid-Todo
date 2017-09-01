package com.nobrain.devfest.reactiveandroid.detail


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.nobrain.devfest.reactiveandroid.R
import com.nobrain.devfest.reactiveandroid.repository.TodoRepository
import kotlinx.android.synthetic.main.act_todo_detail.*

class TodoDetailActivity : AppCompatActivity() {


    private var id: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_todo_detail)

        setUpActionbar()
        initExtras()
        initTodo(id)


    }

    private fun setUpActionbar() {

        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setTitle(R.string.edit_todo)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_menu_save -> {
                val text = et_todo_detail.text.toString()
                save(id, text)
            }
            R.id.action_menu_delete -> delete(id)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun delete(id: Long) {
        if (id > 0) {
            TodoRepository.instance.remove(id)
        }
        finish()
    }

    private fun save(id: Long, text: String) {
        val extraId: Long
        if (id > 0) {
            TodoRepository.instance.updateTodo(id, text)
        } else {
            extraId = System.currentTimeMillis()
            TodoRepository.instance.addTodo(extraId, text)
        }

        finish()

    }

    private fun initTodo(id: Long) {
        if (id > 0) {
            val todo = TodoRepository.instance.get(id)
            et_todo_detail.setText(todo.content)
        }
    }

    private fun initExtras() {
        val intent = intent
        if (intent != null) {
            if (intent.hasExtra("id")) {
                id = intent.getLongExtra("id", -1)
            }
        }
    }
}
