package com.example.tasker

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    // Declare UI elements
    private lateinit var recyclerView: RecyclerView
    private lateinit var add_button: FloatingActionButton
    private lateinit var empty_imageview: ImageView
    private lateinit var no_data: TextView

    // Declare database helper
    private lateinit var myDB: MyDatabaseHelper

    // Initialize arrays to store task data
    var task_id: ArrayList<String>? = null
    var task_title: ArrayList<String>? = null
    var task_description: ArrayList<String>? = null
    var task_date: ArrayList<String>? = null
    var customAdapter: CustomAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        add_button = findViewById<FloatingActionButton>(R.id.add_button)
        empty_imageview = findViewById<ImageView>(R.id.empty_imageview)
        no_data = findViewById<TextView>(R.id.no_data)

        // Set OnClickListener for add_button to open AddActivity
        add_button.setOnClickListener(View.OnClickListener {
            val intent = Intent(
                this@MainActivity,
                AddActivity::class.java
            )
            startActivity(intent)
        })

        // Initialize database helper and arrays
        myDB = MyDatabaseHelper(this@MainActivity)
        task_id = ArrayList()
        task_title = ArrayList()
        task_description = ArrayList()
        task_date = ArrayList()

        // Populate arrays with data from the database
        storeDataInArrays()

        // Initialize and set the adapter for the RecyclerView
        customAdapter = CustomAdapter(
            this@MainActivity, this, task_id!!, task_title!!, task_description!!,
            task_date!!
        )
        recyclerView.setAdapter(customAdapter)
        recyclerView.setLayoutManager(LinearLayoutManager(this@MainActivity))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            recreate()
        }
    }

    // Populate arrays with data from the database
    fun storeDataInArrays() {
        val cursor: Cursor? = myDB.readAllData()
        if (cursor?.count == 0) {
            // If there is no data in the database, show empty view
            empty_imageview!!.visibility = View.VISIBLE
            no_data!!.visibility = View.VISIBLE
        } else {
            // If there is data in the database, populate arrays with data and hide empty view
            while (cursor?.moveToNext()==true) {
                task_id!!.add(cursor.getString(0))
                task_title!!.add(cursor.getString(1))
                task_description!!.add(cursor.getString(2))
                task_date!!.add(cursor.getString(3))
            }
            empty_imageview!!.visibility = View.GONE
            no_data!!.visibility = View.GONE
        }
    }

    // Create options menu (for deleting all data)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Handle options menu item selection (delete all data)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all) {
            confirmDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    // Show confirmation dialog for deleting all data
    private fun confirmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete All?")
        builder.setMessage("Are you sure you want to delete all Data?")
        builder.setPositiveButton(
            "Yes"
        ) { dialogInterface, i ->
            val myDB: MyDatabaseHelper = MyDatabaseHelper(this@MainActivity)
            myDB.deleteAllData()
            //Refresh Activity
            val intent = Intent(
                this@MainActivity,
                MainActivity::class.java
            )
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton(
            "No"
        ) { dialogInterface, i -> }
        builder.create().show()
    }
}