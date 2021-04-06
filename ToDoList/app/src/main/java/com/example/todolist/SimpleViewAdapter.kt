package com.example.todolist


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.*


class SimpleViewAdapter(mContext: Context, mData: NotasDbTable): RecyclerView.Adapter<SimpleViewAdapter.Holder>() {

    var mContext: Context = mContext
    var db : NotasDbTable = mData
    var mData: List<NotaTexto> = db.leerNotas()
    lateinit var myDialog : Dialog
    lateinit var vHolder: Holder



    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tv_titulo.text = mData[position].titulo
        holder.tv_materia.text= mData[position].materia


    }

    override fun getItemCount(): Int {
        return mData.size
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var v: View = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_simple_item, parent, false)
        var vHolder = Holder(v, mContext)
        this.vHolder= vHolder
        myDialog = Dialog(mContext)

        vHolder.item_nota.setOnClickListener(View.OnClickListener{
            myDialog.setContentView(R.layout.activity_view_to_do)
            createDialog()
            var dialog_titulo_et: EditText = myDialog.findViewById(R.id.Titulo)
            var dialog_texto_et: EditText= myDialog.findViewById(R.id.Nota)
            var dialog_materia_tv: TextView = myDialog.findViewById(R.id.Materia)

            //Setea el texto en los editText
            dialog_titulo_et.setText(mData[vHolder.adapterPosition].titulo)
            dialog_texto_et.setText(mData[vHolder.adapterPosition].texto)
            dialog_materia_tv.setText(mData[vHolder.adapterPosition].materia)



            var dialog_fab_edit : FloatingActionButton = myDialog.findViewById(R.id.fab_edit)
            dialog_fab_edit.setOnClickListener {

                var oldTitulo = mData[vHolder.adapterPosition].titulo
                var newTitulo =dialog_titulo_et.text.toString()
                var oldNota= mData[vHolder.adapterPosition].texto
                var newNota = dialog_texto_et.text.toString()
                if (oldTitulo != newTitulo || oldNota != newNota){
                    var newNotaTexto = NotaTexto(mData[vHolder.adapterPosition].materia, newTitulo, newNota)
                    db.update(newNotaTexto , mData[vHolder.adapterPosition])
                }


                finish()

            }

            var dialog_fab_delete: FloatingActionButton = myDialog.findViewById(R.id.fab_delete)
            dialog_fab_delete.setOnClickListener {
                deleteMessage(vHolder.adapterPosition)
            }

            var dialog_back: ImageButton = myDialog.findViewById(R.id.back)
            dialog_back.setOnClickListener{
                myDialog.hide()
            }



            myDialog.show()
        })






        return vHolder
    }



    fun removeItem(pos: Int) {
        var data = mData[pos]
        db.delete(db.getID(mData[pos]))
        mData = db.leerNotas()
        notifyItemRemoved(pos)



    }





    private fun deleteMessage(pos: Int){
        val dialog = AlertDialog.Builder(mContext)
        dialog.setMessage("¿Seguro que quiere borrar esta nota?")
            .setCancelable(false)
            .setPositiveButton("Borrar", DialogInterface.OnClickListener { dialog, id ->
                db.delete(db.getID(mData[pos]))
                finish()
            })
            .setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
            })

        val alert = dialog.create()
        alert.show()
    }

    private  fun finish(){
        var intent= Intent(mContext, MainActivity::class.java)
        var bundle= Bundle()
        startActivity(mContext,intent, bundle)
    }

    private  fun createDialog(){
        var params = WindowManager.LayoutParams()
        params.copyFrom(myDialog.window?.attributes)
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height= WindowManager.LayoutParams.MATCH_PARENT
        myDialog.window?.attributes  = params
    }




    class Holder(itemView: View, context: Context): RecyclerView.ViewHolder(itemView){
        var ItemView= itemView
        var tv_titulo : TextView = ItemView.findViewById(R.id.titulo_nota)
        var tv_materia: TextView = ItemView.findViewById(R.id.nombre_materia)
        var item_nota: ConstraintLayout= ItemView.findViewById(R.id.nota_item)




    }




}