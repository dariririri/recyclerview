package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    //private var counter=0
    private val list = mutableListOf<Todo>()
    private lateinit var adapter: RecyclerAdapter
    val dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        // создаём инстанс адаптера, отдаём ему список
        list.addAll(dbHelper.getAll())
        adapter = RecyclerAdapter(list) {
            // адаптеру передали обработчик удаления элемента
            list.removeAt(it)
            adapter.notifyItemRemoved(it)
        }

        val inputer = findViewById<EditText>(R.id.inputer)
        inputer.showSoftInputOnFocus = false;

        adapter = RecyclerAdapter(list) {
            // адаптеру передали обработчик удаления элемента
            // TODO: удалить элемент из базы по ID
            val id = list[it].id
            dbHelper.remove(id)

            list.removeAt(it)
            adapter.notifyItemRemoved(it)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val keys = findViewById<Button>(R.id.keys)

        recyclerView.setOnClickListener {
            keys.setOnClickListener {
                inputer.showSoftInputOnFocus = true;
            }
        }

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val title = inputer.text.toString()
            val id = dbHelper.addTodo(title)
            list.add(Todo(id, title))
            adapter.notifyItemInserted(list.lastIndex)
            inputer.text.clear()
        }
    }
}