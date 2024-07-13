package com.example.mytodolist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTask : AppCompatActivity() {

    private lateinit var etTask: EditText
    private lateinit var addTaskButton: Button
    private lateinit var dbHelper: DbHelper

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etTask = findViewById(R.id.enter_task)
        addTaskButton = findViewById(R.id.task_add_btn)
        dbHelper = DbHelper(this)

        addTaskButton.setOnClickListener {
            val taskValue = etTask.text.toString()
            if (taskValue.isNotEmpty()) {
                val currentDateTime = getCurrentDateTime()
                val date = currentDateTime.first
                val time = currentDateTime.second

                val isTaskAdded = dbHelper.addTaskDetails(taskValue, date, time)
                if (isTaskAdded) {
                    Toast.makeText(this, "Task added successfully!", Toast.LENGTH_LONG).show()
                    etTask.text.clear()

                    val intent= Intent(this,DisplayTask::class.java)
                        startActivity(intent)
                } else {
                    Toast.makeText(this, "Failed to add task.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please enter a task.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getCurrentDateTime(): Pair<String, String> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val now = Date()
        val date = dateFormat.format(now)
        val time = timeFormat.format(now)
        return Pair(date, time)
    }
}
