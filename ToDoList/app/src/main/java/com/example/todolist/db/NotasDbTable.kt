package com.example.todolist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class NotasDbTable(context: Context) {
    private val dbHelper= toDoListDB(context)
    var order : String = "${Notas._ID} ASC"
    var instance= this

    fun store(nota: NotaTexto): Long {
        val db= dbHelper.writableDatabase

        val values= ContentValues()
        with(values){
            put(Notas.MATERIA_COL, nota.materia)
            put(Notas.TITLE_COL, nota.titulo)
            put(Notas.NOTA_COL, nota.texto)


        }


        val id= db.transaction {
            //Como extendi de SQLiteDATABASE abajo, tengo acceso a todos los metodos de SQLIte
            insert(Notas.TABLE_NAME, null, values)
        }



        db.close()
        return id

    }




    fun update(newNota:NotaTexto, oldNota:NotaTexto){
        var db= dbHelper.writableDatabase
        val values= ContentValues()
        with(values){
            put(Notas.MATERIA_COL, newNota.materia)
            put(Notas.TITLE_COL,newNota.titulo)
            put(Notas.NOTA_COL, newNota.texto)
        }

        var selection = Notas._ID+ " =?"
        var selection_args: Array<String> = arrayOf(getID(oldNota).toString())

            db.update(Notas.TABLE_NAME, values, selection, selection_args)
        db.close()
    }

    fun delete(id:Int){
        var db = dbHelper.writableDatabase
        var selection = Notas._ID+ " =?"
        var selection_args: Array<String> = arrayOf(id.toString())
        db.delete(Notas.TABLE_NAME, selection, selection_args)
        //db.execSQL("DELETE FROM notas WHERE id = "+ID)


        db.close()
    }

    fun leerNotas(): List<NotaTexto>{
        val db= dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + Notas.TABLE_NAME + " ORDER BY " + order, null)

        val notas= mutableListOf<NotaTexto>()
        while (cursor.moveToNext()){
            val materia= cursor.getString(cursor.getColumnIndex(Notas.MATERIA_COL))
            val title = cursor.getString(cursor.getColumnIndex(Notas.TITLE_COL))
            val texto= cursor.getString(cursor.getColumnIndex(Notas.NOTA_COL))
            notas.add(NotaTexto(materia, title, texto))
        }
        cursor.close()
        db.close()

        return notas


    }

    fun getID(nota:NotaTexto): Int{
        val columns = arrayOf(Notas._ID, Notas.TITLE_COL, Notas.MATERIA_COL, Notas.NOTA_COL)
        val db= dbHelper.readableDatabase
        val order = "${Notas._ID} ASC"
        val cursor = db.query(Notas.TABLE_NAME, columns, null, null, null, null, order)
        var id: Int =0
        var encontre= false
        while (cursor.moveToNext() && !encontre){
            val materia= cursor.getString(cursor.getColumnIndex(Notas.MATERIA_COL))
            val title = cursor.getString(cursor.getColumnIndex(Notas.TITLE_COL))
            val texto= cursor.getString(cursor.getColumnIndex(Notas.NOTA_COL))
            if (nota.materia== materia && nota.titulo == title && nota.texto== texto){
                id = cursor.getInt(cursor.getColumnIndex(Notas._ID))
                encontre=true
            }
        }
        cursor.close()

        return id
    }

    fun Materias(): List<String>{
        val columns= arrayOf(Notas.MATERIA_COL)
        val order = "${Notas.MATERIA_COL} ASC"

        val db= dbHelper.readableDatabase
        val cursor = db.query(Notas.TABLE_NAME, columns, null, null, null, null, order )

        val materiaList = mutableListOf<String>()
        while (cursor.moveToNext()) {
            val materia = cursor.getString(cursor.getColumnIndex(Notas.MATERIA_COL))
            if (materia !in materiaList) {
                materiaList.add(materia)
            }
        }
        cursor.close()
        db.close()
        return materiaList
    }


}

private inline fun <T> SQLiteDatabase.transaction(function: SQLiteDatabase.() -> T): T{
    beginTransaction()
    val result = try{
       val returnValue= function()
        setTransactionSuccessful()
        returnValue

    }finally {
        endTransaction()
    }
    close()
    return result
}