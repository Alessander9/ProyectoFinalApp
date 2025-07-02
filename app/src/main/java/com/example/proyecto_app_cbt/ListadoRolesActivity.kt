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
import kotlinx.coroutines.launch

class ListadoRolesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RolAdapter
    private val listaRoles = mutableListOf<Rol>()
    private val rolDao = RolDAOFirestore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_roles)

        // 1. Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewRoles)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 2. Configurar adapter con callbacks
        adapter = RolAdapter(
            listaRoles,
            onItemClick = { rol ->
                val intent = Intent(this, DetalleRolActivity::class.java)
                intent.putExtra("ROL_ID", rol.id)
                startActivity(intent)
            },
            onEditarClick = { rol ->
                val intent = Intent(this, EditarRolActivity::class.java)
                intent.putExtra("ROL_ID", rol.id)
                startActivity(intent)
            },
            onEliminarClick = { rol ->
                // Eliminar usando coroutines
                lifecycleScope.launch {
                    val exito = rolDao.eliminar(rol.id)
                    if (exito) {
                        Toast.makeText(
                            this@ListadoRolesActivity,
                            "Rol eliminado",
                            Toast.LENGTH_SHORT
                        ).show()
                        recargarListaRoles()
                    } else {
                        Toast.makeText(
                            this@ListadoRolesActivity,
                            "Error al eliminar rol",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
        recyclerView.adapter = adapter

        // 3. Carga inicial de roles
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
