package com.example.mytodolist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UpdatedTask : AppCompatActivity() {

    private lateinit var dbHelper: DbHelper
    private lateinit var data:UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_updated_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve taskid from intent extras
        val key = intent.getStringExtra("taskid").toString()



        val edittask = findViewById<EditText>(R.id.edit_task)
        val updatebtn = findViewById<Button>(R.id.update_btn)

        dbHelper = DbHelper(this)

        // Retrieve task details using selectedTaskInfo method
        data = dbHelper.selectedTaskInfo(key)!!

        edittask.setText(data.task)

        updatebtn.setOnClickListener {

            // Retrieve taskid from intent extras
//            val key = intent.getStringExtra("taskid").toString()
            Log.e("MyTaskId", "Received id of update task is: $key")


            val updatevalue=edittask.text.toString()
            if(updatevalue.isEmpty()){
                edittask.error="data should not be null.."
                return@setOnClickListener
            }
            val updatetasksuccess=dbHelper.UpdateTaskDetails(key,updatevalue)

            if(updatetasksuccess)
            {
                Toast.makeText(this,"task updated successfully",Toast.LENGTH_LONG).show()
                edittask.text.clear()
                val intent= Intent(this,DisplayTask::class.java)
                startActivity(intent)
            }else
            {
                Toast.makeText(this,"task updation fails",Toast.LENGTH_LONG).show()
            }
        }
    }
}
