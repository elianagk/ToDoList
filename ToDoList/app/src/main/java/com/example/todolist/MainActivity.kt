package com.example.todolist

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var adapter: SimpleViewAdapter
    lateinit var myrecyclerview : RecyclerView
    var layoutManager = GridLayoutManager(this, 1) //por defecto empieza como lista
    private lateinit var db : NotasDbTable
    var order = "${Notas._ID} ASC" //por default se ordena por fecha
    val order_mat = "${Notas.MATERIA_COL} ASC"
    val order_id = "${Notas._ID} ASC"





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        db = NotasDbTable(this).instance
        myrecyclerview = findViewById(R.id.recyclerView)





        updateRecycler()
        animationRecycler()

        fab.setOnClickListener { view ->
           //estas dos lineas son para q se pase a otra actividad cuando se toca
            val intent= Intent(this, CreateToDoActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onBackPressed() {
       finish()

    }

    override fun onResume() { //se llama cada vez q se pone el main act en la app
        super.onResume()

        updateRecycler()

    }

    fun updateRecycler(){
        db.order= order
        adapter= SimpleViewAdapter(this, db)
        myrecyclerview.layoutManager = layoutManager
        myrecyclerview.adapter= adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemId= item.itemId
        when (itemId){
            R.id.order_id -> {
                order= order_id
                updateRecycler()
            }
            R.id.order_mat ->{
                order = order_mat
                updateRecycler()
            }


        }



        return super.onOptionsItemSelected(item)
    }

    fun animationRecycler(){

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {

                adapter.removeItem(viewHolder.adapterPosition)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                adapter.notifyDataSetChanged()

            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                var dec = RecyclerViewSwipeDecorator.Builder(applicationContext, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive )
                    .addBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                    .addActionIcon(R.drawable.ic_delete_black_24dp)
                    .create()
                    .decorate()
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(myrecyclerview)

    }






}
