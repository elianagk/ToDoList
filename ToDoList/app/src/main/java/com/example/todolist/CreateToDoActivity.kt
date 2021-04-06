package com.example.todolist

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_create_to_do.*

class CreateToDoActivity : AppCompatActivity() {

    lateinit var tituloET : EditText
    lateinit var textoET : EditText
    lateinit var materiaET: AutoCompleteTextView
    lateinit var guardar: FloatingActionButton
    lateinit var titulo : String
    lateinit var texto : String
    lateinit var materia: String




    val db = NotasDbTable(this).instance

    lateinit var listMaterias: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_to_do)
        tituloET = findViewById(R.id.Titulo)
        textoET = findViewById(R.id.Nota)
        materiaET = findViewById(R.id.Materia)
        listMaterias = db.Materias()
        var adapter= ArrayAdapter<String>(this,android.R.layout.select_dialog_item , listMaterias)
        materiaET.setAdapter(adapter)
        guardar = findViewById(R.id.guardar)



        guardar.setOnClickListener {
            guardar()
        }


    }

    override fun onBackPressed() {
        if (!tituloET.text.toString().isEmpty() || !textoET.text.toString().isEmpty()){
            val dialog = AlertDialog.Builder(this)
            dialog.setMessage("¿Desea guardar la nota antes de salir?").setCancelable(false)
                .setPositiveButton("Guardar", DialogInterface.OnClickListener { dialog, id ->
                    guardar()
                })
                .setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, id ->
                    finish()
                })

            val alert = dialog.create()
            alert.show()
        }
        else
            finish()
    }

    private fun guardar(){
        if (materiaET.text.toString().isEmpty()){
            Toast.makeText(this, "Por favor agregue una materia", Toast.LENGTH_SHORT).show()
        }
        else {
            if (tituloET.text.toString().isEmpty()){
                Toast.makeText(this, "Por favor escriba un titulo", Toast.LENGTH_SHORT).show()

            }else {

                titulo = tituloET.text.toString()
                texto = textoET.text.toString()
                materia = materiaET.text.toString()
                var nota = NotaTexto(materia, titulo, texto)
                db.store(nota)

                finish()
            }
        }

    }




}
