package com.example.mynotes


import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity
import kotlinx.android.synthetic.main.activity_add_note.*


class addNoteActivity : CyaneaAppCompatActivity() {

    private var db:SQLiteDatabase? = null
    private var noteId:Int? = null
    private var cursor: Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

    fun deleteNote(noteId:Int) {

        db?.delete("myNotes", "_id=?", arrayOf(noteId.toString()))
        Toast.makeText(this, "Note deleted successfully!", Toast.LENGTH_LONG).show()
        finish()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.delete_note) {

            deleteNote(noteId!!)


        }
        else
            onBackPressed()
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

        if (cursor?.getString(0) == noteTitleText.text.toString() &&
            cursor?.getString(1) == fullNoteText.text.toString())
            finish()
        else {

            db?.update("myNotes", noteValues, "_id=?", arrayOf(noteId.toString()))
            Toast.makeText(this, "Note updated successfully!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cursor?.close()
        db?.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.note_details_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {

        val noteValues = ContentValues()

        if (fullNoteText.text.toString() == "" && noteTitleText.text.toString() == "")
            deleteNote(noteId!!)
        else {

            if (noteTitleText.text.toString() == "" && fullNoteText.text.toString() != "")
                noteValues.put("noteTitle", "Untitled Note")
            else
                noteValues.put("noteTitle", noteTitleText.text.toString())

            noteValues.put("fullNote", fullNoteText.text.toString())

            if (noteId == 0)
                addNote(noteValues)
            else
                updateNote(noteValues)

            super.onBackPressed()
            finish()
        }
    }
}
