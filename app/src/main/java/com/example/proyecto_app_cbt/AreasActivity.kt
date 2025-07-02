package com.example.proyecto_app_cbt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.adapter.AreaAdapter
import com.example.proyecto_app_cbt.dao.AreaDAOFirestore
import com.example.proyecto_app_cbt.model.Area
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AreasActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AreaAdapter
    private lateinit var etBuscar: TextInputEditText
    private lateinit var fabCrear: FloatingActionButton

    private val listaAreas = mutableListOf<Area>()
    private val areaDao = AreaDAOFirestore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_areas)

        // 1. Vincular vistas
        etBuscar     = findViewById(R.id.etBuscarArea)
        recyclerView = findViewById(R.id.recyclerViewAreas)
        fabCrear     = findViewById(R.id.fabCrearArea)

        // 2. Configurar RecyclerView
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
                    val exito = areaDao.eliminar(area.id)
                    if (exito) {
                        Toast.makeText(this@AreasActivity, "Área eliminada", Toast.LENGTH_SHORT).show()
                        recargarAreas()
                    } else {
                        Toast.makeText(this@AreasActivity, "Error al eliminar área", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
        recyclerView.adapter = adapter

        // 3. Búsqueda en tiempo real
        etBuscar.addTextChangedListener(afterTextChanged = { editable ->
            val query = editable.toString()
            val filtrado = listaAreas.filter { it.nombre.contains(query, ignoreCase = true) }
            adapter.actualizarLista(filtrado)
        })

        // 4. Crear nueva área
        fabCrear.setOnClickListener {
            startActivity(Intent(this, CrearAreaActivity::class.java))
        }

        // 5. Carga inicial de datos
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
