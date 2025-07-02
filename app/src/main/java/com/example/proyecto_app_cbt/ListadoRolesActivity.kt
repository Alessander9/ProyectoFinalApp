package com.example.proyecto_app_cbt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.adapter.RolAdapter
import com.example.proyecto_app_cbt.dao.RolDAOFirestore
import com.example.proyecto_app_cbt.model.Rol
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ListadoRolesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabCrearRol: FloatingActionButton
    private lateinit var adapter: RolAdapter
    private val listaRoles = mutableListOf<Rol>()
    private val rolDao = RolDAOFirestore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Inflar el layout que contiene el RecyclerView y el FAB
        setContentView(R.layout.activity_listado_roles)

        // 2. Vincular el FAB y lanzar la Activity de registro de Rol al hacer click
        fabCrearRol = findViewById(R.id.fabCrearRol)
        fabCrearRol.setOnClickListener {
            startActivity(Intent(this, RegistrarRolActivity::class.java))
        }

        // 3. Vincular y configurar el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewRoles)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RolAdapter(
            listaRoles,
            onItemClick = { rol ->
                // ejemplo de detalle
                val intent = Intent(this, DetalleRolActivity::class.java)
                intent.putExtra("ROL_ID", rol.id)
                startActivity(intent)
            },
            onEditarClick = { rol ->
                // ejemplo de edición
                val intent = Intent(this, EditarRolActivity::class.java)
                intent.putExtra("ROL_ID", rol.id)
                startActivity(intent)
            },
            onEliminarClick = { rol ->
                // eliminación via coroutines
                lifecycleScope.launch {
                    val exito = rolDao.eliminar(rol.id)
                    if (exito) {
                        Toast.makeText(this@ListadoRolesActivity, "Rol eliminado", Toast.LENGTH_SHORT).show()
                        recargarListaRoles()
                    } else {
                        Toast.makeText(this@ListadoRolesActivity, "Error al eliminar rol", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
        recyclerView.adapter = adapter

        // 4. Carga inicial de datos
        recargarListaRoles()
    }

    override fun onResume() {
        super.onResume()
        // Cada vez que regresa a esta Activity (por ejemplo, tras crear un rol),
        // recarga la lista para reflejar cualquier cambio.
        recargarListaRoles()
    }

    private fun recargarListaRoles() {
        lifecycleScope.launch {
            val roles = rolDao.obtenerTodos()
            listaRoles.clear()
            listaRoles.addAll(roles)
            adapter.actualizarLista(listaRoles)
        }
    }
}
