package com.example.todolist

import java.io.File

class NotaTexto( mat: String, titulo:String, texto:String) {
    var materia: String= mat
    var titulo: String = titulo
    var texto: String = texto

    fun editTexto(text : String){
        texto= text;
    }

    fun editTitulo(tit: String){
        titulo= tit
    }






}