package com.example.proyecto_app_cbt

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_app_cbt.dao.SolicitudDAO
import com.example.proyecto_app_cbt.helper.AppDBHelper
import com.example.proyecto_app_cbt.model.Solicitud
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate

class DetalleSolicitudActivity  : BaseActivity() {

    private lateinit var tvMotivo: TextView
    private lateinit var tvFechas: TextView
    private lateinit var tvEstadoActual: TextView
    private lateinit var spNuevoEstado: Spinner
    private lateinit var etObservaciones: EditText
    private lateinit var btnRechazar: Button
    private lateinit var btnAceptar: Button

    private lateinit var dao: SolicitudDAO
    private lateinit var solicitud: Solicitud
    private var userId: Int = 0  // Ajusta según tu lógica de sesión

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

        // 2) DAO (writable para actualizar)
        dao = SolicitudDAO(AppDBHelper(this).writableDatabase)

        // 3) Carga la solicitud por ID
        val id = intent.getIntExtra("solicitud_id", 0)
        solicitud = dao.obtenerPorId(id) ?: run {
            finish()
            return
        }

        // 4) Muestra los datos existentes
        tvMotivo.text       = solicitud.motivo
        tvFechas.text       = "${solicitud.fecha_inicio} - ${solicitud.fecha_fin}"
        tvEstadoActual.text = solicitud.estado
        etObservaciones.setText(solicitud.observaciones)

        // 5) Configura el Spinner de estados
        val estados = resources.getStringArray(R.array.estados_solicitud_array)
        spNuevoEstado.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            estados
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        val pos = estados.indexOf(solicitud.estado).takeIf { it >= 0 } ?: 0
        spNuevoEstado.setSelection(pos)

        // 6) Función para procesar la acción
        fun procesarAccion(nuevoEstado: String) {
            AlertDialog.Builder(this)
                .setTitle("Confirmar")
                .setMessage("¿Deseas marcar la solicitud como “$nuevoEstado”?")
                .setPositiveButton("Sí") { _, _ ->
                    solicitud.apply {
                        estado        = nuevoEstado
                        observaciones = etObservaciones.text.toString().trim()
                        fecha_edita   = LocalDate.now()
                        revisado_por  = userId
                    }
                    dao.actualizar(solicitud)
                    Snackbar.make(tvMotivo, "Solicitud $nuevoEstado", Snackbar.LENGTH_LONG).show()
                    setResult(RESULT_OK)
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }

        // 7) Listeners de botones
        btnRechazar.setOnClickListener { procesarAccion("Rechazada") }
        btnAceptar.setOnClickListener  { procesarAccion("Aprobada")  }
    }
}
