package com.example.proyecto_app_cbt

import android.app.Activity
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
import com.example.proyecto_app_cbt.model.Solicitud
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.util.Log

class ListadoSolicitudesActivity : BaseActivity() {

    companion object {
        private const val REQ_NUEVA_SOLICITUD = 1001
    }

    private lateinit var adapter: SolicitudAdapter
    private lateinit var dao: SolicitudDAO
    private var listaBase: List<Solicitud> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_solicitudes)

        dao = SolicitudDAO(AppDBHelper(this).readableDatabase)

        val etBuscar      = findViewById<EditText>(R.id.etBuscar)
        val rvSolicitudes = findViewById<RecyclerView>(R.id.rvSolicitudes)
        val fabCrear      = findViewById<FloatingActionButton>(R.id.fabCrearSolicitud)

        listaBase = dao.obtenerTodos().reversed()
        adapter = SolicitudAdapter(listaBase) { solicitud ->
            Log.d("ListadoSolicitudes", "Clicked solicitud ID=${solicitud.id}")
            startActivity(Intent(this, DetalleSolicitudActivity::class.java).apply {
                putExtra("solicitud_id", solicitud.id)
            })
        }
        rvSolicitudes.layoutManager = LinearLayoutManager(this)
        rvSolicitudes.adapter       = adapter

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val txt  = s.toString().trim().lowercase()
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

        fabCrear.setOnClickListener {
            startActivityForResult(
                Intent(this, SolicitudActivity::class.java),
                REQ_NUEVA_SOLICITUD
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_NUEVA_SOLICITUD && resultCode == Activity.RESULT_OK) {
            listaBase = dao.obtenerTodos().reversed()
            adapter.actualizarLista(listaBase)
        }
    }
}
