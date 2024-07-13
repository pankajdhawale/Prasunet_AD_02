package com.example.mytodolist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DisplayTask : AppCompatActivity() {

    private lateinit var addTaskButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: DbHelper

    private lateinit var showCompleteTask:ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_display_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        addTaskButton = findViewById(R.id.add_btn)
        showCompleteTask=findViewById(R.id.iv_complete)
        dbHelper = DbHelper(this)

        // Fetch all tasks from the database
        val taskList = dbHelper.getAllUserDetails()

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.rv_one)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdaptor(taskList, dbHelper)

        addTaskButton.setOnClickListener {
            val intent = Intent(this, AddTask::class.java)
            startActivity(intent)
        }

        showCompleteTask.setOnClickListener {
           val successIntent=Intent(this,CompleteTask::class.java)
            startActivity(successIntent)
        }

    }
}
