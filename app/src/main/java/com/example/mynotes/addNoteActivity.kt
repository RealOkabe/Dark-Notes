package com.example.mynotes


import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*

class addNoteActivity : AppCompatActivity() {

    private var db:SQLiteDatabase? = null
    private var noteId:Int? = null
    private var cursor: Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        val dbhelper = myNotesSQLiteOpenHelper(this)
        db = dbhelper.writableDatabase

        noteId = intent.extras?.get("noteId").toString().toInt()



        if(noteId != 0) {

            cursor = db?.query("myNotes",
                arrayOf("noteTitle", "fullNote"),
                "_id=?",
                arrayOf(noteId.toString()),
                null, null, null)

            if (cursor?.moveToFirst() == true) {

                noteTitleText.setText(cursor?.getString(0))
                fullNoteText.setText(cursor?.getString(1))

            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.save_note) {

            val noteValues = ContentValues()

            if (noteTitleText.text.toString() == "")
                noteValues.put("noteTitle", "Untitled Note")
            else
                noteValues.put("noteTitle", noteTitleText.text.toString())

            noteValues.put("fullNote", fullNoteText.text.toString())

            if(noteId == 0)
                addNote(noteValues)
            else
                updateNote(noteValues)

        }
        else if(item.itemId == R.id.delete_note) {

            db?.delete("myNotes", "_id=?", arrayOf(noteId.toString()))
            Toast.makeText(this, "Note deleted successfully!", Toast.LENGTH_LONG).show()
            finish()

        }
        return super.onOptionsItemSelected(item)


    }

    private fun addNote(noteValues:ContentValues) {

        db?.insert("myNotes", null, noteValues)

        Toast.makeText(this, "Note Added Successfully!", Toast.LENGTH_SHORT).show()

        noteTitleText.setText("")
        fullNoteText.setText("")
        noteTitleText.requestFocus()
    }

    private fun updateNote(noteValues: ContentValues) {
        db?.update("myNotes", noteValues, "_id=?", arrayOf(noteId.toString()))
        Toast.makeText(this, "Note updated successfully!", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cursor?.close()
        db?.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.note_details_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }
}
