package com.example.proyecto_app_cbt

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_app_cbt.dao.SolicitudDAO
import com.example.proyecto_app_cbt.helper.AppDBHelper
import com.example.proyecto_app_cbt.model.Solicitud
import com.example.proyecto_app_cbt.databinding.ActivitySolicitudBinding
import java.time.LocalDate
import java.util.Calendar

class SolicitudActivity : BaseActivity() {

    private lateinit var binding: ActivitySolicitudBinding
    private lateinit var dao: SolicitudDAO

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySolicitudBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) Inicializa DAO
        val db = AppDBHelper(this).writableDatabase
        dao = SolicitudDAO(db)

        // 2) DatePickers
        binding.etFechaInicio.setOnClickListener { mostrarCalendario(binding.etFechaInicio) }
        binding.etFechaFin.setOnClickListener   { mostrarCalendario(binding.etFechaFin)   }

        // 3) Spinner de tipos
        val tiposVacaciones = listOf(
            "Vacaciones regulares",
            "Adelanto de vacaciones",
            "Sin goce de haber",
            "Vacaciones proporcionales",
            "Permiso especial"
        )
        binding.spTipoVacaciones.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            tiposVacaciones
        )

        // 4) Botón Enviar
        binding.btnEnviar.setOnClickListener {
            // a) Leer datos
            val inicio   = LocalDate.parse(binding.etFechaInicio.text.toString().split("/").let {
                "${it[2]}-${it[1].padStart(2,'0')}-${it[0].padStart(2,'0')}"
            })
            val fin      = LocalDate.parse(binding.etFechaFin.text.toString().split("/").let {
                "${it[2]}-${it[1].padStart(2,'0')}-${it[0].padStart(2,'0')}"
            })
            val tipo     = binding.spTipoVacaciones.selectedItem as String
            val motivo   = binding.etMotivo.text.toString().trim()
            val obs      = binding.etObservaciones.text.toString().trim()
            val hoy      = LocalDate.now()

            // b) Crear objeto con estado inicial “Pendiente” y revisado_por = 0
            val nueva = Solicitud(
                id_usuario  = 1,           // Ajusta al ID real del usuario logueado
                fecha_inicio = inicio,
                fecha_fin    = fin,
                motivo       = "$tipo\n$motivo",
                estado       = "Pendiente",
                observaciones= obs,
                fecha_crea   = hoy,
                fecha_edita  = hoy,
                revisado_por = 0
            )

            // c) Insertar en BD
            dao.insertar(nueva)

            // d) Mostrar diálogo y volver
            AlertDialog.Builder(this)
                .setTitle("Solicitud enviada")
                .setMessage("Tu solicitud ha sido guardada y enviada satisfactoriamente.")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    finish()    // al cerrar regresas al listado
                }
                .show()
        }
    }

    private fun mostrarCalendario(campo: android.widget.EditText) {
        val c = Calendar.getInstance()
        DatePickerDialog(this,
            { _, y, m, d ->
                campo.setText("%02d/%02d/%04d".format(d, m + 1, y))
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
