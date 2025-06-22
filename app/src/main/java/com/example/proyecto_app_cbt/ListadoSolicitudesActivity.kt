package com.example.proyecto_app_cbt

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.adapter.SolicitudAdapter
import com.example.proyecto_app_cbt.dao.SolicitudDAO
import com.example.proyecto_app_cbt.helper.AppDBHelper

class ListadoSolicitudesActivity : AppCompatActivity() {

    private lateinit var adapter: SolicitudAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_solicitudes)

        val etBuscar = findViewById<EditText>(R.id.etBuscar)
        val rvSolicitudes = findViewById<RecyclerView>(R.id.rvSolicitudes)
        val btnCrear = findViewById<Button>(R.id.btnCrearSolicitud)

        val db = AppDBHelper(this).readableDatabase
        val dao = SolicitudDAO(db)
        val listaSolicitudes = dao.obtenerTodos().reversed() // recientes primero

        adapter = SolicitudAdapter(listaSolicitudes)
        rvSolicitudes.layoutManager = LinearLayoutManager(this)
        rvSolicitudes.adapter = adapter

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val filtrado = listaSolicitudes.filter {
                    it.motivo.contains(s.toString(), ignoreCase = true) ||
                            it.estado.contains(s.toString(), ignoreCase = true) ||
                            it.observaciones.contains(s.toString(), ignoreCase = true)
                }
                adapter.actualizarLista(filtrado)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnCrear.setOnClickListener {
            val intent = Intent(this, SolicitudActivity::class.java)
            startActivity(intent)
        }
    }
}
