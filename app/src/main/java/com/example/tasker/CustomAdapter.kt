package com.example.tasker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView


class CustomAdapter(
    private val activity: Activity,
    private val context: Context,
    private val task_id: ArrayList<*>,
    private val task_title: ArrayList<*>,
    private val task_description: ArrayList<*>,
    private val task_date: ArrayList<*>
) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    // Create View Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate layout for each row
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.my_row, parent, false)
        return MyViewHolder(view)
    }


    // Bind data to each view holder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Set data to text views
        holder.task_id_txt.text = task_id[position].toString()
        holder.task_title_txt.text = task_title[position].toString()
        holder.task_description_txt.text = task_description[position].toString()
        holder.task_date_txt.text = task_date[position].toString()
        //Recyclerview onClickListener
        // Set OnClickListener for the entire row
        holder.mainLayout.setOnClickListener {
            // Create intent to update task details
            val intent = Intent(context, UpdateActivity::class.java)
            // Pass task details as extras to the intent
            intent.putExtra("id", task_id[position].toString())
            intent.putExtra("title", task_title[position].toString())
            intent.putExtra("description", task_description[position].toString())
            intent.putExtra("date", task_date[position].toString())
            // Start UpdateActivity and wait for result
            activity.startActivityForResult(intent, 1)
        }
    }

    // Get total number of items in the data set
    override fun getItemCount(): Int {
        return task_id.size
    }


    // View Holder Class
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize views from layout
        var task_id_txt: TextView = itemView.findViewById<TextView>(R.id.task_id_txt)
        var task_title_txt: TextView = itemView.findViewById<TextView>(R.id.task_title_txt)
        var task_description_txt: TextView = itemView.findViewById<TextView>(R.id.task_description_txt)
        var task_date_txt: TextView = itemView.findViewById<TextView>(R.id.task_date_txt)
        var mainLayout: LinearLayout = itemView.findViewById<LinearLayout>(R.id.mainLayout)

        init {
            //Animate Recyclerview
            val translate_anim = AnimationUtils.loadAnimation(
                context, R.anim.translate_anim
            )
            mainLayout.animation = translate_anim
        }
    }
}