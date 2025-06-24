package com.example.proyecto_app_cbt

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.adapter.SolicitudAdapter
import com.example.proyecto_app_cbt.dao.SolicitudDAO
import com.example.proyecto_app_cbt.helper.AppDBHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListadosSolicitudesActivity : AppCompatActivity() {

    private lateinit var adapter: SolicitudAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_solicitudes)

        // 1) Referencias a vistas
        val etBuscar      = findViewById<EditText>(R.id.etBuscar)
        val rvSolicitudes = findViewById<RecyclerView>(R.id.rvSolicitudes)
        val fabCrear      = findViewById<FloatingActionButton>(R.id.fabCrearSolicitud)

        // 2) Inicializa BD y DAO
        val db  = AppDBHelper(this).readableDatabase
        val dao = SolicitudDAO(db)

        // 3) Carga datos (más recientes primero)
        val listaSolicitudes = dao.obtenerTodos().reversed()

        // 4) Configura RecyclerView
        adapter = SolicitudAdapter(listaSolicitudes)
        rvSolicitudes.layoutManager = LinearLayoutManager(this)
        rvSolicitudes.adapter       = adapter

        // 5) Filtro en tiempo real
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val texto = s.toString().trim()
                val filtrado = listaSolicitudes.filter { sol ->
                    sol.motivo.contains(texto, ignoreCase = true) ||
                            sol.estado.contains(texto, ignoreCase = true) ||
                            sol.observaciones.contains(texto, ignoreCase = true)
                }
                adapter.actualizarLista(filtrado)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 6) Navegar a creación de solicitud
        fabCrear.setOnClickListener {
            startActivity(Intent(this, SolicitudActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresca la lista al volver (opcional)
        val updatedList = SolicitudDAO(AppDBHelper(this).readableDatabase)
            .obtenerTodos().reversed()
        adapter.actualizarLista(updatedList)
    }
}
