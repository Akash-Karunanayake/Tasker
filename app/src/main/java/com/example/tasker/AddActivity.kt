package com.example.tasker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat


class AddActivity : AppCompatActivity() {
    private lateinit var title_input: EditText
    private lateinit var description_input: EditText
    private lateinit var date_input: EditText
    private lateinit var add_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        title_input = findViewById(R.id.title_input)
        description_input = findViewById<EditText>(R.id.description_input)
        date_input = findViewById<EditText>(R.id.date_input)
        add_button = findViewById(R.id.add_button)
        add_button.setOnClickListener(View.OnClickListener setOnClickListener@{
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = dateFormat.parse(date_input.text.toString())
            if (date.time == null) {
                date_input.error = "Invalid date format"
                return@setOnClickListener
            }
            val myDB: MyDatabaseHelper = MyDatabaseHelper(this@AddActivity)
            myDB.addTask(title_input.getText().toString().trim { it <= ' ' },
                description_input.getText().toString().trim { it <= ' ' },
                date,
            )

            title_input.text.clear()
            description_input.text.clear()
            date_input.text.clear()
        })
    }
}