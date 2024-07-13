package com.example.mytodolist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class MyAdaptor(private val userList: MutableList<UserModel>, private val dbHelper: DbHelper) : RecyclerView.Adapter<MyAdaptor.DataAdapter>() {

    class DataAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var taskdisplay: TextView = itemView.findViewById(R.id.display_task)
        var datedisplay: TextView = itemView.findViewById(R.id.tv_date)
        var timedisplay: TextView = itemView.findViewById(R.id.tv_time)
        var editimgtask: ImageView = itemView.findViewById(R.id.update_img_btn)
        var deleteimgtask: ImageView = itemView.findViewById(R.id.delete_img_btn)

        var checktask:CheckBox=itemView.findViewById(R.id.check_task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapter {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.single_item, parent, false)
        return DataAdapter(itemView)
    }

    override fun onBindViewHolder(holder: DataAdapter, position: Int) {
        val currentItem = userList[position]
        holder.taskdisplay.text = currentItem.task
        holder.datedisplay.text = currentItem.date
        holder.timedisplay.text = currentItem.time
        holder.checktask.text= currentItem.isCompleted.toString()

        val context = holder.itemView.context

        holder.editimgtask.setOnClickListener {
            val intent = Intent(context, UpdatedTask::class.java)
            val task_id = currentItem.id
            intent.putExtra("taskid", task_id)
            context.startActivity(intent)

        }

      holder.deleteimgtask.setOnClickListener {
            val task_id = currentItem.id
           val success = dbHelper.deleteTask(task_id)
           if (success) {
               userList.removeAt(position)
               notifyItemRemoved(position)
               notifyItemRangeChanged(position, itemCount)
                Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to delete task", Toast.LENGTH_SHORT).show()
            }
        }

        holder.checktask.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
            {
                showCompletionDialogue(context,currentItem)
            }
        }
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showCompletionDialogue(context: Context?, currentItem: UserModel) {

        if (context != null) {
            val builder=AlertDialog.Builder(context)
            builder.setTitle("Complete task")
            builder.setMessage("Do you want to add this task to completed task list?")
            builder.setPositiveButton("yes"){
                dialogue,which->
                currentItem.isCompleted= true
                dbHelper.markTaskAsComplete(currentItem.id)
                notifyDataSetChanged()
                Toast.makeText(context,"Well done,You have completed task successfully",Toast.LENGTH_LONG).show()

            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val alert=builder.create()
            alert.show()
        }
    }
}
