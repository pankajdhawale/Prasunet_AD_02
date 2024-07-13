package com.example.mytodolist

data class UserModel(
    val id: String,
    val task: String,
    val date: String,
    val time: String,
    val completedDate: String? = null,
    val completedTime: String? = null,
    var isCompleted: Boolean=false,
    )
