package com.example.proyecto_app_cbt

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.proyecto_app_cbt.dao.SolicitudDAOFirestore
import com.example.proyecto_app_cbt.model.Solicitud
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class DetalleSolicitudActivity : BaseActivity() {

    private lateinit var tvMotivo: TextView
    private lateinit var tvFechas: TextView
    private lateinit var tvEstadoActual: TextView
    private lateinit var spNuevoEstado: Spinner
    private lateinit var etObservaciones: EditText
    private lateinit var btnRechazar: Button
    private lateinit var btnAceptar: Button

    private val dao = SolicitudDAOFirestore()
    private lateinit var solicitud: Solicitud
    private var userId: String = ""  // Ajusta según tu lógica de sesión

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_solicitud)

        // 1) Referencias a vistas
        tvMotivo        = findViewById(R.id.tvMotivoDet)
        tvFechas        = findViewById(R.id.tvFechasDet)
        tvEstadoActual  = findViewById(R.id.tvEstadoActual)
        spNuevoEstado   = findViewById(R.id.spNuevoEstado)
        etObservaciones = findViewById(R.id.etObservacionesDet)
        btnRechazar     = findViewById(R.id.btnRechazar)
        btnAceptar      = findViewById(R.id.btnAceptar)

        // 2) Obtén el ID de la solicitud desde el intent
        val solicitudId = intent.getStringExtra("solicitud_id") ?: run {
            finish()
            return
        }

        // 3) Carga la solicitud desde Firestore
        lifecycleScope.launch {
            val sol = dao.obtenerPorId(solicitudId)
            if (sol != null) {
                solicitud = sol
                mostrarDatos()
            } else {
                Toast.makeText(this@DetalleSolicitudActivity, "Solicitud no encontrada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // 4) Configura el Spinner de estados
        val estados = resources.getStringArray(R.array.estados_solicitud_array)
        spNuevoEstado.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            estados
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // 5) Listeners de botones
        btnRechazar.setOnClickListener { procesarAccion("Rechazada") }
        btnAceptar.setOnClickListener  { procesarAccion("Aprobada")  }
    }

    private fun mostrarDatos() {
        tvMotivo.text       = solicitud.motivo
        tvFechas.text       = "${solicitud.fecha_inicio?.toDate()?.toLocalDate()} - ${solicitud.fecha_fin?.toDate()?.toLocalDate()}"
        tvEstadoActual.text = solicitud.estado
        etObservaciones.setText(solicitud.observaciones)

        val estados = resources.getStringArray(R.array.estados_solicitud_array)
        val pos = estados.indexOf(solicitud.estado).takeIf { it >= 0 } ?: 0
        spNuevoEstado.setSelection(pos)
    }

    private fun procesarAccion(nuevoEstado: String) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar")
            .setMessage("¿Deseas marcar la solicitud como “$nuevoEstado”?")
            .setPositiveButton("Sí") { _, _ ->
                lifecycleScope.launch {
                    solicitud.apply {
                        estado        = nuevoEstado
                        observaciones = etObservaciones.text.toString().trim()
                        fecha_edita   = com.google.firebase.Timestamp.now()
                        revisado_por  = userId
                    }
                    val exito = dao.actualizar(solicitud)
                    if (exito) {
                        Snackbar.make(tvMotivo, "Solicitud $nuevoEstado", Snackbar.LENGTH_LONG).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Snackbar.make(tvMotivo, "Error al actualizar solicitud", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun Date.toLocalDate(): LocalDate {
        return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }
}