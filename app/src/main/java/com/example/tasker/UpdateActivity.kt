package com.example.tasker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class UpdateActivity : AppCompatActivity() {
    private lateinit var title_input: EditText
    private lateinit var description_input: EditText
    private lateinit var date_input: EditText
    private lateinit var update_button: Button
    private lateinit var delete_button: Button

    private var id: String? = null
    private var title: String? = null
    private var description: String? = null
    private var date: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        title_input = findViewById(R.id.title_input2)
        description_input = findViewById(R.id.description_input2)
        date_input = findViewById(R.id.date_input2)
        update_button = findViewById(R.id.update_button)
        delete_button = findViewById(R.id.delete_button)

        // First we call this
        getAndSetIntentData()

        // Set actionbar title after getAndSetIntentData method
        val ab = supportActionBar
        ab?.title = title

        update_button.setOnClickListener(View.OnClickListener {
            // And only then we call this
            val myDB = MyDatabaseHelper(this@UpdateActivity)
            title = title_input.text.toString().trim()
            description = description_input.text.toString().trim()
            val updatedDate = SimpleDateFormat("yyyy-MM-dd").parse(date_input.text.toString().trim())
            myDB.updateData(id!!, title, description, updatedDate)
        })
        delete_button.setOnClickListener(View.OnClickListener { confirmDialog() })
    }

    private fun getAndSetIntentData() {
        if (intent.hasExtra("id") && intent.hasExtra("title") &&
            intent.hasExtra("description") && intent.hasExtra("date")
        ) {
            // Getting Data from Intent
            id = intent.getStringExtra("id")
            title = intent.getStringExtra("title")
            description = intent.getStringExtra("description")
            // Retrieving date as a string from the intent
            val dateString = intent.getStringExtra("date")
            // Parsing the string to a Date object
            date = dateString?.let {
                try {
                    // Assuming date format here, replace with actual date parsing logic
                    // This is just an example, replace with your actual date parsing logic
                    SimpleDateFormat("yyyy-MM-dd").parse(it)
                } catch (e: ParseException) {
                    null // Handle parsing error here
                }
            }

            // Setting Intent Data
            title_input.setText(title)
            description_input.setText(description)
            // Setting date to the EditText
            date?.let { date_input.setText(SimpleDateFormat("yyyy-MM-dd").format(it)) }
            Log.d("stev", "$title $description $date")
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete $title ?")
        builder.setMessage("Are you sure you want to delete $title ?")
        builder.setPositiveButton(
            "Yes"
        ) { dialogInterface, i ->
            val myDB = MyDatabaseHelper(this@UpdateActivity)
            myDB.deleteOneRow(id!!)
            finish()
        }
        builder.setNegativeButton(
            "No"
        ) { dialogInterface, i -> }
        builder.create().show()
    }
}
