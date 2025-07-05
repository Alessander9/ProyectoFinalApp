package com.example.proyecto_app_cbt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.adapter.AreaAdapter
import com.example.proyecto_app_cbt.dao.AreaDAOFirestore
import com.example.proyecto_app_cbt.model.Area
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ListadoAreaActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AreaAdapter
    private lateinit var fabCrear: FloatingActionButton

    private val listaAreas = mutableListOf<Area>()
    private val areaDao = AreaDAOFirestore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_area)

        // 1. Vincular vistas
        recyclerView = findViewById(R.id.recyclerViewAreas)
        fabCrear = findViewById(R.id.fabCrearArea)

        // 2. Configurar RecyclerView y Adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AreaAdapter(
            listaAreas,
            onItemClick = { area ->
                val intent = Intent(this, DetalleAreaActivity::class.java)
                intent.putExtra("AREA_ID", area.id)
                startActivity(intent)
            },
            onEditarClick = { area ->
                val intent = Intent(this, EditarAreaActivity::class.java)
                intent.putExtra("AREA_ID", area.id)
                startActivity(intent)
            },
            onEliminarClick = { area ->
                lifecycleScope.launch {
                    val success = areaDao.eliminar(area.id)
                    if (success) {
                        Toast.makeText(this@ListadoAreaActivity, "Área eliminada", Toast.LENGTH_SHORT).show()
                        recargarAreas()
                    } else {
                        Toast.makeText(this@ListadoAreaActivity, "Error al eliminar área", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
        recyclerView.adapter = adapter

        // 3. Botón para crear nueva área
        fabCrear.setOnClickListener {
            val intent = Intent(this, CrearAreaActivity::class.java)
            startActivity(intent)
        }

        // 4. Carga inicial de datos
        recargarAreas()
    }

    private fun recargarAreas() {
        lifecycleScope.launch {
            val areas = areaDao.obtenerTodos()
            listaAreas.clear()
            listaAreas.addAll(areas)
            adapter.actualizarLista(listaAreas)
        }
    }
}
