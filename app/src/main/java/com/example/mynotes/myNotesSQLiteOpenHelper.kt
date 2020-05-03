package com.example.mynotes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class myNotesSQLiteOpenHelper(context: Context): SQLiteOpenHelper(context, "myNotesDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE myNotes(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "noteTitle TEXT," +
                "fullNote TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}