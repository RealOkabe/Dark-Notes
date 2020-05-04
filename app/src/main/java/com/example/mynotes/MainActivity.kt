package com.example.mynotes

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SimpleCursorAdapter
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var db:SQLiteDatabase? = null
    private var cursor:Cursor? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fun startAddNoteActivity(noteId:Long) {

            val intent = Intent(this, addNoteActivity::class.java)
            intent.putExtra("noteId", noteId)
            startActivity(intent)

        }



        addNoteButton.setOnClickListener {

            startAddNoteActivity(0)

        }

        notesListView.setOnItemClickListener { _, _, _, id ->

            startAddNoteActivity(id)

        }

    }

    override fun onStart() {
        super.onStart()
        // Create Database.
        val dbCreate = myNotesSQLiteOpenHelper(this)

        db = dbCreate.readableDatabase

        // Cursor is needed to retrieve data from the database for the list view to use.
        cursor = db?.query("myNotes", arrayOf("_id", "noteTitle"),
            null, null, null, null, null)

        // This acts as an intermediary for the list view and passes the data retrieved by the cursor.
        val listAdapter = SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, arrayOf("noteTitle"),
            intArrayOf(android.R.id.text1), 0)

        notesListView.adapter = listAdapter
    }


    override fun onDestroy() {
        super.onDestroy()
        cursor?.close()
        db?.close()
    }
}
