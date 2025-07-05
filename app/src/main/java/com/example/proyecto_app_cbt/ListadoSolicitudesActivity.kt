package com.example.proyecto_app_cbt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_app_cbt.adapter.SolicitudAdapter
import com.example.proyecto_app_cbt.dao.SolicitudDAOFirestore
import com.example.proyecto_app_cbt.model.Solicitud
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ListadoSolicitudesActivity : BaseActivity() {

    companion object {
        private const val REQ_NUEVA_SOLICITUD = 1001
    }

    private lateinit var adapter: SolicitudAdapter
    private val dao = SolicitudDAOFirestore()
    private var listaBase: List<Solicitud> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_solicitudes)

        val prefs = getSharedPreferences("dataUser", MODE_PRIVATE)
        val rolNombre = prefs.getString("rolNombre", "Sin rol") ?: "Sin rol"

        val etBuscar = findViewById<EditText>(R.id.etBuscar)
        val fabCrear = findViewById<FloatingActionButton>(R.id.fabCrearSolicitud)
        val btnVerCalendario = findViewById<Button>(R.id.btnVerCalendario)

        btnVerCalendario.setOnClickListener {
            val intent = Intent(this, CalendarioActivity::class.java)
            startActivity(intent)
        }

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val txt = s.toString().trim().lowercase()
                val filt = listaBase.filter {
                    it.motivo.lowercase().contains(txt) ||
                            it.estado.lowercase().contains(txt) ||
                            it.observaciones.lowercase().contains(txt)
                }
                adapter.actualizarLista(filt)
            }
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
        })

        if (rolNombre == "Trabajador") {
            fabCrear.visibility = View.VISIBLE
            fabCrear.setOnClickListener {
                startActivityForResult(
                    Intent(this, SolicitudActivity::class.java),
                    REQ_NUEVA_SOLICITUD
                )
            }
        } else {
            fabCrear.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        val rvSolicitudes = findViewById<RecyclerView>(R.id.rvSolicitudes)

        lifecycleScope.launch {
            listaBase = dao.obtenerTodos().reversed()
            Log.d("ListadoSolicitudes", "Lista cargada: ${listaBase.size} solicitudes")

            adapter = SolicitudAdapter(listaBase) { solicitud ->
                Log.d("ListadoSolicitudes", "Clicked solicitud ID=${solicitud.id}")
                startActivity(Intent(this@ListadoSolicitudesActivity, DetalleSolicitudActivity::class.java).apply {
                    putExtra("solicitud_id", solicitud.id) // ID como String
                })
            }
            rvSolicitudes.layoutManager = LinearLayoutManager(this@ListadoSolicitudesActivity)
            rvSolicitudes.adapter = adapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_NUEVA_SOLICITUD && resultCode == Activity.RESULT_OK) {
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