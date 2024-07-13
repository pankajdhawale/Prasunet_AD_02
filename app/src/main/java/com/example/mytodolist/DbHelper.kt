package com.example.mytodolist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TaskDb"
        private const val DATABASE_VERSION = 1
    }

    private lateinit var sqLiteDatabase: SQLiteDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE TaskInfo(id INTEGER PRIMARY KEY AUTOINCREMENT, task TEXT, date TEXT, time TEXT,isCompleted INTEGER DEFAULT 0,completedDate TEXT DEFAULT '',completedTime TEXT DEFAULT '')"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS TaskInfo")
        onCreate(db)
    }

    fun addTaskDetails(task: String, date: String, time: String): Boolean {
        sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("task", task)
            put("date", date)
            put("time", time)
        }
        val result = sqLiteDatabase.insert("TaskInfo", null, contentValues)
        return result != -1L
    }

    fun getAllUserDetails(): ArrayList<UserModel> {
        sqLiteDatabase = this.readableDatabase
        val cursor: Cursor = sqLiteDatabase.rawQuery("SELECT * FROM TaskInfo", null)
        val taskList = ArrayList<UserModel>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val task = cursor.getString(cursor.getColumnIndexOrThrow("task"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))

                val userModel = UserModel(id.toString(), task, date, time)
                taskList.add(userModel)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return taskList
    }

    fun selectedTaskInfo(id: String): UserModel? {
        sqLiteDatabase = this.readableDatabase
        val cursor: Cursor = sqLiteDatabase.rawQuery("SELECT * FROM TaskInfo WHERE id = ?", arrayOf(id.toString()))
        var userModel: UserModel? = null
        if (cursor.moveToFirst()) {
            val taskId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val task = cursor.getString(cursor.getColumnIndexOrThrow("task"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
            userModel = UserModel(taskId.toString(), task, date, time)
        }
        cursor.close()
        return userModel
    }

    fun deleteTask(id: String):Boolean {
        sqLiteDatabase = this.writableDatabase
        return sqLiteDatabase.delete("TaskInfo", "id=?", arrayOf(id.toString())) > 0
    }
    fun UpdateTaskDetails(id:String,task: String): Boolean {
        sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("task", task)
        }
        val result= sqLiteDatabase.update("TaskInfo", contentValues,"id=?", arrayOf(id))

        if(result>0)
        {
            return true
        }else{
            return false
        }

    }
    fun markTaskAsComplete(id:String): Boolean {

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

        sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues().apply {

            put("isCompleted", 1) // Assuming 1 means completed and 0 means not completed
            put("completedDate", currentDate)
            put("completedTime", currentTime)
        }
        val result = sqLiteDatabase.update("TaskInfo", contentValues, "id=?", arrayOf(id.toString()))
        return result > 0
    }

    fun getCompletedTasks(): ArrayList<UserModel> {
        sqLiteDatabase = this.readableDatabase
        val cursor: Cursor = sqLiteDatabase.rawQuery("SELECT * FROM TaskInfo WHERE isCompleted = 1", null)
        val taskList = ArrayList<UserModel>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val task = cursor.getString(cursor.getColumnIndexOrThrow("task"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
                val completedDate = cursor.getString(cursor.getColumnIndexOrThrow("completedDate"))
                val completedTime = cursor.getString(cursor.getColumnIndexOrThrow("completedTime"))

                val userModel = UserModel(id.toString(), task, date, time,completedDate, completedTime)
                taskList.add(userModel)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return taskList
    }
}
