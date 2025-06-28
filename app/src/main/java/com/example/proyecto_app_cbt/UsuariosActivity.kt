package com.example.proyecto_app_cbt

import UsuarioAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.dao.UsuarioDAOFirestore
import com.example.proyecto_app_cbt.model.Usuario
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class UsuariosActivity : BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAgregar: FloatingActionButton
    private lateinit var usuarioAdapter: UsuarioAdapter
    private val usuarios = mutableListOf<Usuario>()
    private val usuarioDAOFirestore = UsuarioDAOFirestore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)

        recyclerView = findViewById(R.id.recyclerUsuarios)
        fabAgregar = findViewById(R.id.fabAgregarUsuario)

        usuarioAdapter = UsuarioAdapter(usuarios,
            onEditar = { usuario ->
                Toast.makeText(this, "Editar: ${usuario.nombre_completo}", Toast.LENGTH_SHORT).show()
                // Aquí podrías abrir una actividad de edición pasando el usuario
            },
            onInactivar = { usuario ->
                usuario.activo = !usuario.activo
                val estado = if (usuario.activo) "activo" else "inactivo"
                Toast.makeText(this, "${usuario.nombre_completo} ahora está $estado", Toast.LENGTH_SHORT).show()

                lifecycleScope.launch {
                    val actualizado = usuarioDAOFirestore.actualizar(usuario)
                    if (actualizado) {
                        Toast.makeText(this@UsuariosActivity, "Estado actualizado en Firestore", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = usuarioAdapter

        fabAgregar.setOnClickListener {
            val intent = Intent(this, RegistrarUsuarioActivity::class.java)
            startActivity(intent)
        }

        // 🔹 Cargar usuarios desde Firestore
        obtenerUsuarios()
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