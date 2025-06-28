package com.example.proyecto_app_cbt

import UsuarioAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.dao.UsuarioDAO
import com.example.proyecto_app_cbt.helper.AppDBHelper
import com.example.proyecto_app_cbt.model.Usuario
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UsuariosActivity : BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAgregar: FloatingActionButton
    private lateinit var usuarioAdapter: UsuarioAdapter
    private lateinit var usuarioDAO: UsuarioDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)

        recyclerView = findViewById(R.id.recyclerUsuarios)
        fabAgregar = findViewById(R.id.fabAgregarUsuario)

        val dbHelper = AppDBHelper(this)
        usuarioDAO = UsuarioDAO(dbHelper.readableDatabase)

        val usuarios: MutableList<Usuario> = usuarioDAO.obtenerTodos().toMutableList()

        usuarioAdapter = UsuarioAdapter(usuarios,
            onEditar = { usuario ->
                Toast.makeText(this, "Editar: ${usuario.nombre_completo}", Toast.LENGTH_SHORT).show()
            },
            onInactivar = { usuario ->
                usuario.activo = !usuario.activo
                val estado = if (usuario.activo) "activo" else "inactivo"
                Toast.makeText(this, "${usuario.nombre_completo} ahora está $estado", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = usuarioAdapter


                fabAgregar.setOnClickListener {
                    // Crear el Intent hacia RegistrarUsuarioActivity
                    val intent = Intent(this, RegistrarUsuarioActivity::class.java)
                    startActivity(intent)
                }

    }
}