package com.example.todolist

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class toDoListDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

   private val SQL_CREATE_ENTRIES = "CREATE TABLE ${Notas.TABLE_NAME} (" +
           "${Notas._ID} INTEGER PRIMARY KEY AUTOINCREMENT ," +
           "${Notas.MATERIA_COL} TEXT, "+
           "${Notas.TITLE_COL} TEXT, "+
           "${Notas.NOTA_COL} TEXT" +
           ")"

    private val SQL_DELETE_ENTRIES= "DROP TABLE IF EXISTS ${Notas.TABLE_NAME}"




    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }










}