package com.example.mytodolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.zip.Inflater

class MyAdaptor2(context: Context, private val userList: MutableList<UserModel>) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return userList.size
    }

    override fun getItem(position: Int): Any {
        return userList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.complete_task_ui, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val user = userList[position]
        viewHolder.taskTextView.text = user.task
        viewHolder.dateTextView.text = user.completedDate
        viewHolder.timeTextView.text = user.completedTime

        return view
    }

    private class ViewHolder(view: View) {
        val taskTextView: TextView = view.findViewById(R.id.complete_task)
        val dateTextView: TextView = view.findViewById(R.id.complete_task_date)
        val timeTextView: TextView = view.findViewById(R.id.complete_task_time)
    }
}