package com.example.todolist

import android.provider.BaseColumns

val DATABASE_NAME = "toDoListCategory.db"
val DATABASE_VERSION= 10

object Notas : BaseColumns{
    val TABLE_NAME = "notas"
    val _ID= "id"
    val MATERIA_COL= "materia"
    val TITLE_COL= "titulo"
    val NOTA_COL = "nota"


}