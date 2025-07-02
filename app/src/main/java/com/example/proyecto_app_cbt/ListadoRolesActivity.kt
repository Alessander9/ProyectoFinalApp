package com.example.proyecto_app_cbt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.adapter.RolAdapter
import com.example.proyecto_app_cbt.dao.RolDAOFirestore
import com.example.proyecto_app_cbt.model.Rol
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ListadoRolesActivity : BaseActivity() {

    companion object {
        private const val REQ_NUEVO_ROL = 2001
    }

    private lateinit var adapter: RolAdapter
    private val dao = RolDAOFirestore()
    private var listaBase: List<Rol> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_roles)

        val etBuscar = findViewById<EditText>(R.id.etBuscar)
        val fabCrear = findViewById<FloatingActionButton>(R.id.fabCrearRol)

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val txt = s.toString().trim().lowercase()
                val filt = listaBase.filter {
                    it.nombre.lowercase().contains(txt)
                }
                adapter.actualizarLista(filt)
            }
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
        })

        fabCrear.setOnClickListener {
            startActivityForResult(
                Intent(this, RolActivity::class.java),
                REQ_NUEVO_ROL
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val rvRoles = findViewById<RecyclerView>(R.id.rvRoles)

        lifecycleScope.launch {
            listaBase = dao.obtenerTodos().reversed()
            Log.d("ListadoRoles", "Lista cargada: ${listaBase.size} roles")

            adapter = RolAdapter(
                listaBase,
                onItemClick = { rol ->
                    // Clic en todo el ítem: Detalle
                    Log.d("ListadoRoles", "Clicked rol ID=${rol.id} (detalle)")
                    startActivity(Intent(this@ListadoRolesActivity, DetalleRolActivity::class.java).apply {
                        putExtra("rol_id", rol.id)
                    })
                },
                onEditarClick = { rol ->
                    // Clic en botón Editar
                    Log.d("ListadoRoles", "Clicked rol ID=${rol.id} (editar)")
                    startActivity(Intent(this@ListadoRolesActivity, RolActivity::class.java).apply {
                        putExtra("rol_id", rol.id)
                    })
                }
            )
            rvRoles.layoutManager = LinearLayoutManager(this@ListadoRolesActivity)
            rvRoles.adapter = adapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_NUEVO_ROL && resultCode == Activity.RESULT_OK) {
            actualizarLista()
        }
    }

    private fun actualizarLista() {
        lifecycleScope.launch {
            listaBase = dao.obtenerTodos().reversed()
            adapter.actualizarLista(listaBase)
        }
    }
}