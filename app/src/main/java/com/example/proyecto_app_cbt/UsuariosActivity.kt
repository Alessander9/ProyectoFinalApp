package com.example.proyecto_app_cbt

import UsuarioAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.dao.UsuarioDAOFirestore
import com.example.proyecto_app_cbt.dao.AreaDAOFirestore
import com.example.proyecto_app_cbt.dao.RolDAOFirestore
import com.example.proyecto_app_cbt.model.Area
import com.example.proyecto_app_cbt.model.Rol
import com.example.proyecto_app_cbt.model.Usuario
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class UsuariosActivity : BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAgregar: FloatingActionButton
    private lateinit var usuarioAdapter: UsuarioAdapter
    private val usuarios = mutableListOf<Usuario>()
    private val usuarioDAOFirestore = UsuarioDAOFirestore()
    private val areaDao = AreaDAOFirestore()
    private val rolDao = RolDAOFirestore()
    private var listaAreas: List<Area> = emptyList()
    private var listaRoles: List<Rol> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)

        recyclerView = findViewById(R.id.recyclerUsuarios)
        fabAgregar = findViewById(R.id.fabAgregarUsuario)

        usuarioAdapter = UsuarioAdapter(usuarios,
            onEditar = { usuario ->
                val intent = Intent(this, RegistrarUsuarioActivity::class.java)
                intent.putExtra("usuarioId", usuario.id)
                startActivity(intent)
            },
            onInactivar = { usuario ->
                usuario.activo = !usuario.activo
                val estado = if (usuario.activo) "activo" else "inactivo"

                lifecycleScope.launch {
                    val actualizado = usuarioDAOFirestore.actualizar(usuario)
                    if (actualizado) {
                        Toast.makeText(this@UsuariosActivity, "Estado actualizado en Firestore", Toast.LENGTH_SHORT).show()
                        obtenerUsuarios()
                    }
                }
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = usuarioAdapter

        fabAgregar.setOnClickListener {
            val intent = Intent(this, RegistrarUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        lifecycleScope.launch {
            listaAreas = areaDao.obtenerTodos()
            listaRoles = rolDao.obtenerTodos()
            usuarioAdapter.actualizarListas(listaAreas, listaRoles)
            obtenerUsuarios()
        }
    }

    private fun obtenerUsuarios() {
        lifecycleScope.launch {
            val listaUsuarios = usuarioDAOFirestore.obtenerTodos()
            usuarios.clear()
            usuarios.addAll(listaUsuarios)
            usuarioAdapter.notifyDataSetChanged()
        }
    }
}